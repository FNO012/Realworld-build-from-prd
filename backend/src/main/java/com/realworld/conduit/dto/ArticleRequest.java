package com.realworld.conduit.dto;

import jakarta.validation.constraints.NotBlank;

public class ArticleRequest {
    
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    
    @NotBlank(message = "설명은 필수입니다.")
    private String description;
    
    @NotBlank(message = "내용은 필수입니다.")
    private String body;
    
    public ArticleRequest() {}
    
    public ArticleRequest(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
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
}