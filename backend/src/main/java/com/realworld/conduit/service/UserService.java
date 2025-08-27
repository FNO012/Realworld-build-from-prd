package com.realworld.conduit.service;

import com.realworld.conduit.dto.UserRegistrationRequest;
import com.realworld.conduit.dto.UserResponse;
import com.realworld.conduit.dto.UserUpdateRequest;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.mapper.UserFollowMapper;
import com.realworld.conduit.model.User;
import com.realworld.conduit.model.UserFollow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserMapper userMapper;
    private final UserFollowMapper userFollowMapper;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        log.debug("회원가입 시도: email={}, username={}", request.getEmail(), request.getUsername());
        
        if (userMapper.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        if (userMapper.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + request.getUsername());
        }
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();
        
        userMapper.insertUser(user);
        
        log.info("회원가입 완료: userId={}, username={}", user.getId(), user.getUsername());
        
        return UserResponse.from(user);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + email);
        }
        return UserResponse.from(user);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + username);
        }
        return UserResponse.from(user);
    }
    
    @Transactional
    public UserResponse updateUser(String userEmail, UserUpdateRequest request) {
        User user = userMapper.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userEmail);
        }
        
        // Update fields if provided
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            // Check if username is already taken by another user
            if (!user.getUsername().equals(request.getUsername()) && userMapper.existsByUsername(request.getUsername())) {
                throw new RuntimeException("이미 존재하는 사용자명입니다: " + request.getUsername());
            }
            user.setUsername(request.getUsername());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // Check if email is already taken by another user
            if (!user.getEmail().equals(request.getEmail()) && userMapper.existsByEmail(request.getEmail())) {
                throw new RuntimeException("이미 존재하는 이메일입니다: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getImage() != null) {
            user.setImage(request.getImage());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }
        
        userMapper.updateUser(user);
        
        User updatedUser = userMapper.findByEmail(user.getEmail());
        return UserResponse.from(updatedUser);
    }
    
    // 로그인은 Spring Security에서 처리됩니다.
    // CustomUserDetailsService를 통해 사용자 인증이 수행됩니다.
    
    // ===== Follow/Unfollow 기능 =====
    
    @Transactional
    public UserResponse followUser(String currentUserEmail, String targetUsername) {
        User currentUser = userMapper.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("현재 사용자를 찾을 수 없습니다: " + currentUserEmail);
        }
        
        User targetUser = userMapper.findByUsername(targetUsername);
        if (targetUser == null) {
            throw new RuntimeException("팔로우할 사용자를 찾을 수 없습니다: " + targetUsername);
        }
        
        if (currentUser.getId().equals(targetUser.getId())) {
            throw new RuntimeException("자기 자신을 팔로우할 수 없습니다");
        }
        
        // 이미 팔로우하고 있는지 확인
        if (userFollowMapper.isFollowing(currentUser.getId(), targetUser.getId())) {
            throw new RuntimeException("이미 팔로우하고 있는 사용자입니다");
        }
        
        UserFollow userFollow = new UserFollow(currentUser.getId(), targetUser.getId());
        userFollowMapper.insertFollow(userFollow);
        
        log.info("팔로우 완료: {} -> {}", currentUser.getUsername(), targetUser.getUsername());
        
        UserResponse response = UserResponse.from(targetUser);
        response.setFollowing(true);
        response.setFollowersCount(userFollowMapper.countFollowers(targetUser.getId()));
        response.setFollowingCount(userFollowMapper.countFollowing(targetUser.getId()));
        
        return response;
    }
    
    @Transactional
    public UserResponse unfollowUser(String currentUserEmail, String targetUsername) {
        User currentUser = userMapper.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("현재 사용자를 찾을 수 없습니다: " + currentUserEmail);
        }
        
        User targetUser = userMapper.findByUsername(targetUsername);
        if (targetUser == null) {
            throw new RuntimeException("언팔로우할 사용자를 찾을 수 없습니다: " + targetUsername);
        }
        
        // 팔로우하고 있는지 확인
        if (!userFollowMapper.isFollowing(currentUser.getId(), targetUser.getId())) {
            throw new RuntimeException("팔로우하지 않고 있는 사용자입니다");
        }
        
        userFollowMapper.deleteFollow(currentUser.getId(), targetUser.getId());
        
        log.info("언팔로우 완료: {} -> {}", currentUser.getUsername(), targetUser.getUsername());
        
        UserResponse response = UserResponse.from(targetUser);
        response.setFollowing(false);
        response.setFollowersCount(userFollowMapper.countFollowers(targetUser.getId()));
        response.setFollowingCount(userFollowMapper.countFollowing(targetUser.getId()));
        
        return response;
    }
    
    @Transactional(readOnly = true)
    public boolean isFollowing(String currentUserEmail, String targetUsername) {
        if (currentUserEmail == null || targetUsername == null) {
            return false;
        }
        
        User currentUser = userMapper.findByEmail(currentUserEmail);
        User targetUser = userMapper.findByUsername(targetUsername);
        
        if (currentUser == null || targetUser == null) {
            return false;
        }
        
        return userFollowMapper.isFollowing(currentUser.getId(), targetUser.getId());
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserProfile(String targetUsername, String currentUserEmail) {
        User targetUser = userMapper.findByUsername(targetUsername);
        if (targetUser == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + targetUsername);
        }
        
        UserResponse response = UserResponse.from(targetUser);
        response.setFollowersCount(userFollowMapper.countFollowers(targetUser.getId()));
        response.setFollowingCount(userFollowMapper.countFollowing(targetUser.getId()));
        
        // 현재 사용자가 인증된 경우 팔로우 상태 설정
        if (currentUserEmail != null) {
            response.setFollowing(isFollowing(currentUserEmail, targetUsername));
        } else {
            response.setFollowing(false);
        }
        
        return response;
    }
}