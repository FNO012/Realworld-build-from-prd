package com.realworld.conduit.service;

import com.realworld.conduit.dto.ArticleRequest;
import com.realworld.conduit.dto.ArticleResponse;
import com.realworld.conduit.mapper.ArticleFavoriteMapper;
import com.realworld.conduit.mapper.ArticleMapper;
import com.realworld.conduit.mapper.ArticleTagMapper;
import com.realworld.conduit.mapper.TagMapper;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.model.Article;
import com.realworld.conduit.model.ArticleFavorite;
import com.realworld.conduit.model.ArticleTag;
import com.realworld.conduit.model.Tag;
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
    private final ArticleFavoriteMapper articleFavoriteMapper;
    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;
    private final UserMapper userMapper;
    
    @Autowired
    public ArticleService(ArticleMapper articleMapper, ArticleFavoriteMapper articleFavoriteMapper, ArticleTagMapper articleTagMapper, TagMapper tagMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.articleFavoriteMapper = articleFavoriteMapper;
        this.articleTagMapper = articleTagMapper;
        this.tagMapper = tagMapper;
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
        ArticleResponse response = new ArticleResponse(createdArticle);
        response.setFavoritesCount(getFavoritesCount(createdArticle.getId()));
        return response;
    }
    
    @Transactional(readOnly = true)
    public Article getArticleModelBySlug(String slug) {
        return articleMapper.findBySlug(slug);
    }
    
    @Transactional(readOnly = true)
    public Long getArticleIdBySlug(String slug) {
        Article article = articleMapper.findBySlug(slug);
        return article != null ? article.getId() : null;
    }
    
    @Transactional(readOnly = true)
    public ArticleResponse getArticle(String slug) {
        Article article = articleMapper.findBySlug(slug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        ArticleResponse response = new ArticleResponse(article);
        response.setFavoritesCount(getFavoritesCount(article.getId()));
        // favorited will be false by default, will be set by controller if user is authenticated
        return response;
    }
    
    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles(int offset, int limit) {
        List<Article> articles = articleMapper.findAllArticles(offset, limit);
        return articles.stream()
                .map(article -> {
                    ArticleResponse response = new ArticleResponse(article);
                    response.setFavoritesCount(getFavoritesCount(article.getId()));
                    return response;
                })
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
        ArticleResponse response = new ArticleResponse(updatedArticle);
        response.setFavoritesCount(getFavoritesCount(updatedArticle.getId()));
        return response;
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
    
    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticlesByAuthor(String authorUsername, int offset, int limit) {
        List<Article> articles = articleMapper.findArticlesByAuthor(authorUsername, offset, limit);
        return articles.stream()
                .map(article -> {
                    ArticleResponse response = new ArticleResponse(article);
                    response.setFavoritesCount(getFavoritesCount(article.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticlesByTag(String tag, int offset, int limit) {
        List<Article> articles = articleMapper.findArticlesByTag(tag, offset, limit);
        return articles.stream()
                .map(article -> {
                    ArticleResponse response = new ArticleResponse(article);
                    response.setFavoritesCount(getFavoritesCount(article.getId()));
                    return response;
                })
                .collect(Collectors.toList());
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
    
    public void favoriteArticle(String slug, String userEmail) {
        Article article = articleMapper.findBySlug(slug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        
        User user = userMapper.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        
        // Check if already favorited
        if (!articleFavoriteMapper.isFavorited(article.getId(), user.getId())) {
            ArticleFavorite favorite = new ArticleFavorite(article.getId(), user.getId());
            articleFavoriteMapper.insertFavorite(favorite);
        }
    }
    
    public void unfavoriteArticle(String slug, String userEmail) {
        Article article = articleMapper.findBySlug(slug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        
        User user = userMapper.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        
        articleFavoriteMapper.deleteFavorite(article.getId(), user.getId());
    }
    
    public int getFavoritesCount(Long articleId) {
        return articleFavoriteMapper.countFavorites(articleId);
    }
    
    public boolean isFavorited(Long articleId, String userEmail) {
        User user = userMapper.findByEmail(userEmail);
        if (user == null) {
            return false;
        }
        
        return articleFavoriteMapper.isFavorited(articleId, user.getId());
    }
}