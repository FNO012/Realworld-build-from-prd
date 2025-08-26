package com.realworld.conduit.controller;

import com.realworld.conduit.dto.ApiResponse;
import com.realworld.conduit.dto.UserLoginRequest;
import com.realworld.conduit.dto.UserRegistrationRequest;
import com.realworld.conduit.dto.UserResponse;
import com.realworld.conduit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRegistrationRequest request,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .success(false)
                    .error(ApiResponse.ErrorDetails.builder()
                            .code("VALIDATION_ERROR")
                            .message("입력 데이터가 유효하지 않습니다")
                            .details(errors)
                            .build())
                    .build();
            
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            UserResponse userResponse = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(userResponse));
        } catch (RuntimeException e) {
            log.error("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("REGISTRATION_FAILED", e.getMessage()));
        }
    }
    
    // 로그인은 Spring Security의 /login 엔드포인트를 사용합니다.
    // POST /login 요청으로 email, password를 form-data로 전송하세요.
    // Content-Type: application/x-www-form-urlencoded
    // Body: email=user@example.com&password=yourpassword
}