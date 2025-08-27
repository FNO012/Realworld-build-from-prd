package com.realworld.conduit.model;

import java.time.LocalDateTime;

public class UserFollow {
    private Long id;
    private Long followerId;    // 팔로우하는 사용자 ID
    private Long followingId;   // 팔로우당하는 사용자 ID
    private LocalDateTime createdAt;
    
    // 조인용 필드들
    private String followerUsername;
    private String followingUsername;
    
    public UserFollow() {}
    
    public UserFollow(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFollowerId() {
        return followerId;
    }
    
    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }
    
    public Long getFollowingId() {
        return followingId;
    }
    
    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getFollowerUsername() {
        return followerUsername;
    }
    
    public void setFollowerUsername(String followerUsername) {
        this.followerUsername = followerUsername;
    }
    
    public String getFollowingUsername() {
        return followingUsername;
    }
    
    public void setFollowingUsername(String followingUsername) {
        this.followingUsername = followingUsername;
    }
}