package com.realworld.conduit.service;

import com.realworld.conduit.dto.ArticleRequest;
import com.realworld.conduit.dto.ArticleResponse;
import com.realworld.conduit.mapper.ArticleMapper;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.model.Article;
import com.realworld.conduit.model.User;
import com.realworld.conduit.util.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    
    @Autowired
    public ArticleService(ArticleMapper articleMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
    }
    
    public ArticleResponse createArticle(ArticleRequest request, String userEmail) {
        User user = userMapper.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        
        String baseSlug = SlugUtils.generateSlug(request.getTitle());
        String uniqueSlug = generateUniqueSlug(baseSlug);
        
        Article article = new Article(uniqueSlug, request.getTitle(), 
                                    request.getDescription(), request.getBody(), user.getId());
        
        articleMapper.insertArticle(article);
        
        Article createdArticle = articleMapper.findBySlug(uniqueSlug);
        return new ArticleResponse(createdArticle);
    }
    
    @Transactional(readOnly = true)
    public ArticleResponse getArticle(String slug) {
        Article article = articleMapper.findBySlug(slug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        return new ArticleResponse(article);
    }
    
    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles(int offset, int limit) {
        List<Article> articles = articleMapper.findAllArticles(offset, limit);
        return articles.stream()
                .map(ArticleResponse::new)
                .collect(Collectors.toList());
    }
    
    public ArticleResponse updateArticle(String slug, ArticleRequest request, String userEmail) {
        Article existingArticle = articleMapper.findBySlug(slug);
        if (existingArticle == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        
        User user = userMapper.findByEmail(userEmail);
        if (user == null || !user.getId().equals(existingArticle.getAuthorId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        
        existingArticle.setTitle(request.getTitle());
        existingArticle.setDescription(request.getDescription());
        existingArticle.setBody(request.getBody());
        
        articleMapper.updateArticle(existingArticle);
        
        Article updatedArticle = articleMapper.findBySlug(slug);
        return new ArticleResponse(updatedArticle);
    }
    
    public void deleteArticle(String slug, String userEmail) {
        Article article = articleMapper.findBySlug(slug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        
        User user = userMapper.findByEmail(userEmail);
        if (user == null || !user.getId().equals(article.getAuthorId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        articleMapper.deleteBySlug(slug);
    }
    
    @Transactional(readOnly = true)
    public int getTotalArticlesCount() {
        return articleMapper.countAllArticles();
    }
    
    private String generateUniqueSlug(String baseSlug) {
        String candidate = baseSlug;
        int attempt = 0;
        
        while (articleMapper.existsBySlug(candidate)) {
            attempt++;
            candidate = baseSlug + "-" + attempt;
        }
        
        return candidate;
    }
}