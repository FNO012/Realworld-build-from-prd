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
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody UserLoginRequest request,
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
            UserResponse userResponse = userService.loginUser(request);
            return ResponseEntity.ok(ApiResponse.success(userResponse));
        } catch (RuntimeException e) {
            log.error("로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("LOGIN_FAILED", e.getMessage()));
        }
    }
}