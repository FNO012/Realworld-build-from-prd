package com.realworld.conduit.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {
    
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String body;
    
    public CommentRequest() {}
    
    public CommentRequest(String body) {
        this.body = body;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}