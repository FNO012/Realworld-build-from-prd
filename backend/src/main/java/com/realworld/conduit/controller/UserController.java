package com.realworld.conduit.controller;

import com.realworld.conduit.dto.ApiResponse;
import com.realworld.conduit.dto.UserLoginRequest;
import com.realworld.conduit.dto.UserRegistrationRequest;
import com.realworld.conduit.dto.UserResponse;
import com.realworld.conduit.dto.UserUpdateRequest;
import com.realworld.conduit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            UserResponse userResponse = userService.getUserByEmail(userEmail);
            return ResponseEntity.ok(ApiResponse.success(userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("USER_FETCH_FAILED", "사용자 정보를 가져오는데 실패했습니다"));
        }
    }
    
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(
            @PathVariable String username,
            Authentication authentication) {
        try {
            String currentUserEmail = authentication != null ? authentication.getName() : null;
            UserResponse userResponse = userService.getUserProfile(username, currentUserEmail);
            return ResponseEntity.ok(ApiResponse.success(userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("USER_NOT_FOUND", "사용자를 찾을 수 없습니다"));
        }
    }
    
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            @Valid @RequestBody UserUpdateRequest request,
            BindingResult bindingResult,
            Authentication authentication) {
        
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
            String userEmail = authentication.getName();
            UserResponse userResponse = userService.updateUser(userEmail, request);
            return ResponseEntity.ok(ApiResponse.success(userResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("USER_UPDATE_FAILED", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("USER_UPDATE_FAILED", e.getMessage()));
        }
    }
    
    // ===== Follow/Unfollow API =====
    
    @PostMapping("/{username}/follow")
    public ResponseEntity<ApiResponse<Map<String, UserResponse>>> followUser(
            @PathVariable String username,
            Authentication authentication) {
        
        try {
            String currentUserEmail = authentication.getName();
            UserResponse userResponse = userService.followUser(currentUserEmail, username);
            
            Map<String, UserResponse> data = new HashMap<>();
            data.put("profile", userResponse);
            
            return ResponseEntity.ok(ApiResponse.success(data, "사용자를 팔로우했습니다."));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("FOLLOW_FAILED", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("FOLLOW_FAILED", "팔로우 처리 중 오류가 발생했습니다."));
        }
    }
    
    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ApiResponse<Map<String, UserResponse>>> unfollowUser(
            @PathVariable String username,
            Authentication authentication) {
        
        try {
            String currentUserEmail = authentication.getName();
            UserResponse userResponse = userService.unfollowUser(currentUserEmail, username);
            
            Map<String, UserResponse> data = new HashMap<>();
            data.put("profile", userResponse);
            
            return ResponseEntity.ok(ApiResponse.success(data, "사용자를 언팔로우했습니다."));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("UNFOLLOW_FAILED", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("UNFOLLOW_FAILED", "언팔로우 처리 중 오류가 발생했습니다."));
        }
    }
    
    // 로그인은 Spring Security의 /login 엔드포인트를 사용합니다.
    // POST /login 요청으로 email, password를 form-data로 전송하세요.
    // Content-Type: application/x-www-form-urlencoded
    // Body: email=user@example.com&password=yourpassword
}