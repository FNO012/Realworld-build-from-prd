package com.realworld.conduit.service;

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
    
    // 로그인은 Spring Security에서 처리됩니다.
    // CustomUserDetailsService를 통해 사용자 인증이 수행됩니다.
}