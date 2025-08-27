package com.realworld.conduit.dto;

import com.realworld.conduit.model.Article;
import java.time.LocalDateTime;

public class ArticleResponse {
    private String slug;
    private String title;
    private String description;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AuthorResponse author;
    private int favoritesCount;
    private boolean favorited;
    
    public ArticleResponse() {}
    
    public ArticleResponse(Article article) {
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        
        this.author = new AuthorResponse();
        this.author.setUsername(article.getAuthorUsername());
        this.author.setBio(article.getAuthorBio());
        this.author.setImage(article.getAuthorImage());
        
        // Default values for favorites (will be set by service)
        this.favoritesCount = 0;
        this.favorited = false;
    }
    
    // Getters and setters
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
    
    public AuthorResponse getAuthor() {
        return author;
    }
    
    public void setAuthor(AuthorResponse author) {
        this.author = author;
    }
    
    public int getFavoritesCount() {
        return favoritesCount;
    }
    
    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }
    
    public boolean isFavorited() {
        return favorited;
    }
    
    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
    
    public static class AuthorResponse {
        private String username;
        private String bio;
        private String image;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getBio() {
            return bio;
        }
        
        public void setBio(String bio) {
            this.bio = bio;
        }
        
        public String getImage() {
            return image;
        }
        
        public void setImage(String image) {
            this.image = image;
        }
    }
}