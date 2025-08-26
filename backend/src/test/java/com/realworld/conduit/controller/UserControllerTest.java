package com.realworld.conduit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realworld.conduit.dto.UserLoginRequest;
import com.realworld.conduit.dto.UserRegistrationRequest;
import com.realworld.conduit.dto.UserResponse;
import com.realworld.conduit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRegistrationRequest registrationRequest;
    private UserLoginRequest loginRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        registrationRequest = UserRegistrationRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        loginRequest = UserLoginRequest.builder()
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
    void register_Success() throws Exception {
        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    @DisplayName("회원가입 API 검증 실패 테스트 - 빈 사용자명")
    void register_ValidationFail_EmptyUsername() throws Exception {
        registrationRequest.setUsername("");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("회원가입 API 검증 실패 테스트 - 잘못된 이메일 형식")
    void register_ValidationFail_InvalidEmail() throws Exception {
        registrationRequest.setEmail("invalid-email");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("회원가입 API 비즈니스 로직 실패 테스트")
    void register_BusinessLogicFail() throws Exception {
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenThrow(new RuntimeException("이미 존재하는 이메일입니다"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("REGISTRATION_FAILED"));
    }

    @Test
    @DisplayName("로그인 API 성공 테스트")
    void login_Success() throws Exception {
        when(userService.loginUser(any(UserLoginRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    @DisplayName("로그인 API 검증 실패 테스트 - 빈 이메일")
    void login_ValidationFail_EmptyEmail() throws Exception {
        loginRequest.setEmail("");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("로그인 API 비즈니스 로직 실패 테스트")
    void login_BusinessLogicFail() throws Exception {
        when(userService.loginUser(any(UserLoginRequest.class)))
                .thenThrow(new RuntimeException("존재하지 않는 이메일입니다"));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("LOGIN_FAILED"));
    }
}