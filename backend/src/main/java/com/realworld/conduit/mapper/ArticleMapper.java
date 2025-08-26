package com.realworld.conduit.mapper;

import com.realworld.conduit.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    
    void insertArticle(Article article);
    
    Article findBySlug(@Param("slug") String slug);
    
    List<Article> findAllArticles(@Param("offset") int offset, @Param("limit") int limit);
    
    void updateArticle(Article article);
    
    void deleteBySlug(@Param("slug") String slug);
    
    boolean existsBySlug(@Param("slug") String slug);
    
    int countAllArticles();
}