package com.realworld.conduit.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String body;
    private Long articleId;
    private Long authorId;
    private LocalDateTime createdAt;
    
    // 작성자 정보를 담는 필드 (조인용)
    private String authorUsername;
    private String authorEmail;
    private String authorBio;
    private String authorImage;
    
    public Comment() {}
    
    public Comment(String body, Long articleId, Long authorId) {
        this.body = body;
        this.articleId = articleId;
        this.authorId = authorId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public Long getArticleId() {
        return articleId;
    }
    
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
    
    public Long getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getAuthorUsername() {
        return authorUsername;
    }
    
    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
    
    public String getAuthorEmail() {
        return authorEmail;
    }
    
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
    
    public String getAuthorBio() {
        return authorBio;
    }
    
    public void setAuthorBio(String authorBio) {
        this.authorBio = authorBio;
    }
    
    public String getAuthorImage() {
        return authorImage;
    }
    
    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }
}