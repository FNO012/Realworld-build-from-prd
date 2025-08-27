package com.realworld.conduit.dto;

import com.realworld.conduit.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String image;
    
    // 팔로우 관련 필드
    private boolean following;
    private int followersCount;
    private int followingCount;
    
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .following(false)  // 기본값, 서비스에서 설정
                .followersCount(0)  // 기본값, 서비스에서 설정
                .followingCount(0)  // 기본값, 서비스에서 설정
                .build();
    }
}