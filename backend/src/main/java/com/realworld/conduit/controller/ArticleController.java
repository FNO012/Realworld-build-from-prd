package com.realworld.conduit.controller;

import com.realworld.conduit.dto.ApiResponse;
import com.realworld.conduit.dto.ArticleRequest;
import com.realworld.conduit.dto.ArticleResponse;
import com.realworld.conduit.service.ArticleService;
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
@RequestMapping("/api/articles")
public class ArticleController {
    
    private final ArticleService articleService;
    
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, ArticleResponse>>> createArticle(
            @Valid @RequestBody ArticleRequest request, 
            Authentication authentication) {
        
        try {
            String userEmail = authentication.getName();
            ArticleResponse article = articleService.createArticle(request, userEmail);
            
            Map<String, ArticleResponse> data = new HashMap<>();
            data.put("article", article);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(data, "아티클이 성공적으로 생성되었습니다."));
                    
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ARTICLE_CREATE_FAILED", e.getMessage()));
        }
    }
    
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Map<String, ArticleResponse>>> getArticle(@PathVariable String slug) {
        try {
            ArticleResponse article = articleService.getArticle(slug);
            
            Map<String, ArticleResponse> data = new HashMap<>();
            data.put("article", article);
            
            return ResponseEntity.ok(ApiResponse.success(data));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ARTICLE_NOT_FOUND", e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getArticles(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {
        
        try {
            List<ArticleResponse> articles = articleService.getArticles(offset, limit);
            int totalCount = articleService.getTotalArticlesCount();
            
            Map<String, Object> data = new HashMap<>();
            data.put("articles", articles);
            data.put("articlesCount", totalCount);
            
            return ResponseEntity.ok(ApiResponse.success(data));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ARTICLES_FETCH_FAILED", e.getMessage()));
        }
    }
    
    @PutMapping("/{slug}")
    public ResponseEntity<ApiResponse<Map<String, ArticleResponse>>> updateArticle(
            @PathVariable String slug,
            @Valid @RequestBody ArticleRequest request,
            Authentication authentication) {
        
        try {
            String userEmail = authentication.getName();
            ArticleResponse article = articleService.updateArticle(slug, request, userEmail);
            
            Map<String, ArticleResponse> data = new HashMap<>();
            data.put("article", article);
            
            return ResponseEntity.ok(ApiResponse.success(data, "아티클이 성공적으로 수정되었습니다."));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ARTICLE_UPDATE_FAILED", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{slug}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(
            @PathVariable String slug,
            Authentication authentication) {
        
        try {
            String userEmail = authentication.getName();
            articleService.deleteArticle(slug, userEmail);
            
            return ResponseEntity.ok(ApiResponse.success(null, "아티클이 성공적으로 삭제되었습니다."));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ARTICLE_DELETE_FAILED", e.getMessage()));
        }
    }
}