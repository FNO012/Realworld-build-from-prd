package com.realworld.conduit.dto;

import com.realworld.conduit.model.Comment;
import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private AuthorResponse author;
    
    public CommentResponse() {}
    
    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.body = comment.getBody();
        this.createdAt = comment.getCreatedAt();
        
        this.author = new AuthorResponse();
        this.author.setUsername(comment.getAuthorUsername());
        this.author.setBio(comment.getAuthorBio());
        this.author.setImage(comment.getAuthorImage());
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public AuthorResponse getAuthor() {
        return author;
    }
    
    public void setAuthor(AuthorResponse author) {
        this.author = author;
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