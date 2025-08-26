package com.realworld.conduit.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        String secret = "realworld-conduit-secret-key-for-jwt-token-generation-should-be-at-least-256-bits";
        long tokenValidityInSeconds = 3600; // 1시간
        
        jwtTokenProvider = new JwtTokenProvider(secret, tokenValidityInSeconds);
        
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
        
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    @DisplayName("JWT 토큰 생성 테스트")
    void generateToken_Success() {
        String token = jwtTokenProvider.generateToken(authentication);
        
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT는 3개 부분으로 구성
    }

    @Test
    @DisplayName("JWT 토큰에서 이메일 추출 테스트")
    void getEmailFromToken_Success() {
        String token = jwtTokenProvider.generateToken(authentication);
        
        String email = jwtTokenProvider.getEmailFromToken(token);
        
        assertThat(email).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("유효한 JWT 토큰 검증 테스트")
    void validateToken_ValidToken() {
        String token = jwtTokenProvider.generateToken(authentication);
        
        boolean isValid = jwtTokenProvider.validateToken(token);
        
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("잘못된 JWT 토큰 검증 테스트")
    void validateToken_InvalidToken() {
        String invalidToken = "invalid.jwt.token";
        
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("JWT 토큰 만료시간 추출 테스트")
    void getExpirationFromToken_Success() {
        String token = jwtTokenProvider.generateToken(authentication);
        
        Date expiration = jwtTokenProvider.getExpirationFromToken(token);
        Date now = new Date();
        
        assertThat(expiration).isAfter(now);
        assertThat(expiration.getTime() - now.getTime()).isLessThan(3600 * 1000 + 1000); // 1시간 + 1초 여유
    }
}