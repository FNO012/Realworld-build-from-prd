package com.realworld.conduit.controller;

import com.realworld.conduit.dto.UserRegistrationRequest;
import com.realworld.conduit.dto.UserResponse;
import com.realworld.conduit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private UserRegistrationRequest registrationRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        registrationRequest = UserRegistrationRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();
    }

    @Test
    @DisplayName("회원가입 API 성공 테스트")
    void register_Success() {
        // given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(userResponse);

        // when
        ResponseEntity<?> response = userController.register(registrationRequest, bindingResult);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(userService).registerUser(any(UserRegistrationRequest.class));
    }

    @Test
    @DisplayName("회원가입 API 검증 실패 테스트 - 빈 사용자명")
    void register_ValidationFail_EmptyUsername() {
        // given
        when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = new FieldError("userRegistrationRequest", "username", "사용자명은 필수입니다");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // when
        ResponseEntity<?> response = userController.register(registrationRequest, bindingResult);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("회원가입 API 검증 실패 테스트 - 잘못된 이메일 형식")
    void register_ValidationFail_InvalidEmail() {
        // given
        when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = new FieldError("userRegistrationRequest", "email", "올바른 이메일 형식이 아닙니다");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // when
        ResponseEntity<?> response = userController.register(registrationRequest, bindingResult);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("회원가입 API 비즈니스 로직 실패 테스트")
    void register_BusinessLogicFail() {
        // given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenThrow(new RuntimeException("이미 존재하는 이메일입니다"));

        // when
        ResponseEntity<?> response = userController.register(registrationRequest, bindingResult);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(userService).registerUser(any(UserRegistrationRequest.class));
    }
}