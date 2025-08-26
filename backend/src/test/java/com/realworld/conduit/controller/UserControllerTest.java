package com.realworld.conduit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    // 로그인 테스트는 Spring Security의 /login 엔드포인트를 통해 수행됩니다.
    // 통합 테스트에서 실제 로그인 기능을 테스트할 수 있습니다.
    
    @Test
    @DisplayName("로그인 엔드포인트 접근 가능성 테스트")
    void login_EndpointAccessible() throws Exception {
        // Spring Security 기본 로그인 엔드포인트가 접근 가능한지 확인
        mockMvc.perform(post("/login")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().isUnauthorized()); // 실제 사용자가 없으므로 401 반환
    }
}