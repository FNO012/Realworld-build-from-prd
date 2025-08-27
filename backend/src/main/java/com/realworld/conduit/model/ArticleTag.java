package com.realworld.conduit.model;

import java.time.LocalDateTime;

public class ArticleTag {
    private Long id;
    private Long articleId;
    private Long tagId;
    private LocalDateTime createdAt;
    
    public ArticleTag() {}
    
    public ArticleTag(Long articleId, Long tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
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
    
    public Long getTagId() {
        return tagId;
    }
    
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}