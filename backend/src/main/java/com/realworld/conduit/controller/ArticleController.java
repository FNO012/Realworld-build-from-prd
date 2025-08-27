package com.realworld.conduit.controller;

import com.realworld.conduit.dto.ApiResponse;
import com.realworld.conduit.dto.ArticleRequest;
import com.realworld.conduit.dto.ArticleResponse;
import com.realworld.conduit.model.Article;
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

            // Set favorited status for the author
            article.setFavorited(false); // Authors don't automatically favorite their own articles

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
    public ResponseEntity<ApiResponse<Map<String, ArticleResponse>>> getArticle(@PathVariable String slug, Authentication authentication) {
        try {
            ArticleResponse article = articleService.getArticle(slug);

            // Set favorited status if user is authenticated
            if (authentication != null && authentication.isAuthenticated()) {
                String userEmail = authentication.getName();
                Article existingArticle = articleService.getArticleModelBySlug(slug);
                if (existingArticle != null) {
                    article.setFavorited(articleService.isFavorited(existingArticle.getId(), userEmail));
                }
            }

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
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String tag,
            Authentication authentication) {

        try {
            List<ArticleResponse> articles;
            int totalCount;

            if (author != null && !author.isEmpty()) {
                articles = articleService.getArticlesByAuthor(author, offset, limit);
                // For simplicity, we're not implementing a separate count method for author-specific articles
                totalCount = articles.size();
            } else if (tag != null && !tag.isEmpty()) {
                articles = articleService.getArticlesByTag(tag, offset, limit);
                // For simplicity, we're not implementing a separate count method for tag-specific articles
                totalCount = articles.size();
            } else {
                articles = articleService.getArticles(offset, limit);
                totalCount = articleService.getTotalArticlesCount();
            }

            // Set favorited status for authenticated users
            if (authentication != null && authentication.isAuthenticated()) {
                String userEmail = authentication.getName();
                for (ArticleResponse article : articles) {
                    // Get the article ID by slug
                    Long articleId = articleService.getArticleIdBySlug(article.getSlug());
                    if (articleId != null) {
                        article.setFavorited(articleService.isFavorited(articleId, userEmail));
                    }
                }
            }

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

            // Set favorited status
            Long articleId = articleService.getArticleIdBySlug(slug);
            if (articleId != null) {
                article.setFavorited(articleService.isFavorited(articleId, userEmail));
            }

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

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<ApiResponse<Map<String, ArticleResponse>>> favoriteArticle(
            @PathVariable String slug,
            Authentication authentication) {

        try {
            String userEmail = authentication.getName();
            articleService.favoriteArticle(slug, userEmail);

            ArticleResponse article = articleService.getArticle(slug);
            article.setFavorited(true); // Set to true since we just favorited it

            Map<String, ArticleResponse> data = new HashMap<>();
            data.put("article", article);

            return ResponseEntity.ok(ApiResponse.success(data, "아티클이 성공적으로 즐겨찾기에 추가되었습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ARTICLE_FAVORITE_FAILED", e.getMessage()));
        }
    }

    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<ApiResponse<Map<String, ArticleResponse>>> unfavoriteArticle(
            @PathVariable String slug,
            Authentication authentication) {

        try {
            String userEmail = authentication.getName();
            articleService.unfavoriteArticle(slug, userEmail);

            ArticleResponse article = articleService.getArticle(slug);
            article.setFavorited(false); // Set to false since we just unfavorited it

            Map<String, ArticleResponse> data = new HashMap<>();
            data.put("article", article);

            return ResponseEntity.ok(ApiResponse.success(data, "아티클이 성공적으로 즐겨찾기에서 제거되었습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ARTICLE_UNFAVORITE_FAILED", e.getMessage()));
        }
    }
}