package com.realworld.conduit.service;

import com.realworld.conduit.dto.UserRegistrationRequest;
import com.realworld.conduit.dto.UserResponse;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationRequest registrationRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        registrationRequest = UserRegistrationRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .build();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerUser_Success() {
        when(userMapper.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(userMapper.existsByUsername(registrationRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword");
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return null;
        }).when(userMapper).insertUser(any(User.class));

        UserResponse result = userService.registerUser(registrationRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");

        verify(userMapper).existsByEmail("test@example.com");
        verify(userMapper).existsByUsername("testuser");
        verify(passwordEncoder).encode("password123");
        verify(userMapper).insertUser(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 실패")
    void registerUser_EmailExists() {
        when(userMapper.existsByEmail(registrationRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(registrationRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 존재하는 이메일입니다: test@example.com");

        verify(userMapper).existsByEmail("test@example.com");
        verify(userMapper, never()).insertUser(any());
    }

    @Test
    @DisplayName("이미 존재하는 사용자명으로 회원가입 실패")
    void registerUser_UsernameExists() {
        when(userMapper.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(userMapper.existsByUsername(registrationRequest.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(registrationRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 존재하는 사용자명입니다: testuser");

        verify(userMapper).existsByEmail("test@example.com");
        verify(userMapper).existsByUsername("testuser");
        verify(userMapper, never()).insertUser(any());
    }

    // 로그인 기능은 Spring Security에서 CustomUserDetailsService를 통해 처리됩니다.
    // CustomUserDetailsService에 대한 별도 테스트를 작성해주세요.
}