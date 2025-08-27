package com.realworld.conduit.model;

import java.time.LocalDateTime;

public class ArticleFavorite {
    private Long id;
    private Long articleId;
    private Long userId;
    private LocalDateTime createdAt;
    
    public ArticleFavorite() {}
    
    public ArticleFavorite(Long articleId, Long userId) {
        this.articleId = articleId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getArticleId() {
        return articleId;
    }
    
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}