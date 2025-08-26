package com.realworld.conduit.model;

import java.time.LocalDateTime;

public class Article {
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // User 정보를 담는 필드 (조인용)
    private String authorUsername;
    private String authorEmail;
    private String authorBio;
    private String authorImage;
    
    public Article() {}
    
    public Article(String slug, String title, String description, String body, Long authorId) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.authorId = authorId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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