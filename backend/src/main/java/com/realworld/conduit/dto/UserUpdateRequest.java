package com.realworld.conduit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String image;
    
    @Size(min = 2, max = 50, message = "사용자명은 2-50자 사이여야 합니다")
    private String username;
    
    private String bio;
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    @Size(min = 6, max = 100, message = "비밀번호는 6-100자 사이여야 합니다")
    private String password;
}