package com.realworld.conduit.service;

import com.realworld.conduit.dto.UserLoginRequest;
import com.realworld.conduit.dto.UserRegistrationRequest;
import com.realworld.conduit.dto.UserResponse;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.model.User;
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
    public UserResponse loginUser(UserLoginRequest request) {
        log.debug("로그인 시도: email={}", request.getEmail());
        
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("존재하지 않는 이메일입니다: " + request.getEmail());
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        
        log.info("로그인 성공: userId={}, username={}", user.getId(), user.getUsername());
        
        return UserResponse.from(user);
    }
}