package com.realworld.conduit.service;

import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .build();
    }

    @Test
    @DisplayName("사용자 이메일로 UserDetails 로드 성공")
    void loadUserByUsername_Success() {
        when(userMapper.findByEmail("test@example.com")).thenReturn(mockUser);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");

        verify(userMapper).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 UserDetails 로드 실패")
    void loadUserByUsername_UserNotFound() {
        when(userMapper.findByEmail("nonexistent@example.com")).thenReturn(null);

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("nonexistent@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다: nonexistent@example.com");

        verify(userMapper).findByEmail("nonexistent@example.com");
    }
}