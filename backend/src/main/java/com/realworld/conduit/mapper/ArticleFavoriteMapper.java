package com.realworld.conduit.mapper;

import com.realworld.conduit.model.ArticleFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleFavoriteMapper {
    
    void insertFavorite(ArticleFavorite favorite);
    
    void deleteFavorite(@Param("articleId") Long articleId, @Param("userId") Long userId);
    
    boolean isFavorited(@Param("articleId") Long articleId, @Param("userId") Long userId);
    
    int countFavorites(@Param("articleId") Long articleId);
}