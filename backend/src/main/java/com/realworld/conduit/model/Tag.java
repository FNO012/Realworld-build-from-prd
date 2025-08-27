package com.realworld.conduit.model;

import java.time.LocalDateTime;

public class Tag {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    
    public Tag() {}
    
    public Tag(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}