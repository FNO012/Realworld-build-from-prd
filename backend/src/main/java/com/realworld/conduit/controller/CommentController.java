package com.realworld.conduit.controller;

import com.realworld.conduit.dto.ApiResponse;
import com.realworld.conduit.dto.CommentRequest;
import com.realworld.conduit.dto.CommentResponse;
import com.realworld.conduit.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles/{slug}/comments")
public class CommentController {
    
    private final CommentService commentService;
    
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, CommentResponse>>> createComment(
            @PathVariable String slug,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        
        try {
            String userEmail = authentication.getName();
            CommentResponse comment = commentService.createComment(slug, request, userEmail);
            
            Map<String, CommentResponse> data = new HashMap<>();
            data.put("comment", comment);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(data, "댓글이 성공적으로 작성되었습니다."));
                    
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("COMMENT_CREATE_FAILED", e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComments(@PathVariable String slug) {
        try {
            List<CommentResponse> comments = commentService.getComments(slug);
            int commentsCount = commentService.getCommentsCount(slug);
            
            Map<String, Object> data = new HashMap<>();
            data.put("comments", comments);
            data.put("commentsCount", commentsCount);
            
            return ResponseEntity.ok(ApiResponse.success(data));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("COMMENTS_FETCH_FAILED", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable String slug,
            @PathVariable Long id,
            Authentication authentication) {
        
        try {
            String userEmail = authentication.getName();
            commentService.deleteComment(slug, id, userEmail);
            
            return ResponseEntity.ok(ApiResponse.success(null, "댓글이 성공적으로 삭제되었습니다."));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("COMMENT_DELETE_FAILED", e.getMessage()));
        }
    }
}