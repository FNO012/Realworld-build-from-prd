package com.realworld.conduit.mapper;

import com.realworld.conduit.model.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleTagMapper {
    
    void insertArticleTag(ArticleTag articleTag);
    
    void deleteArticleTag(@Param("articleId") Long articleId, @Param("tagId") Long tagId);
    
    List<ArticleTag> findArticleTagsByArticleId(@Param("articleId") Long articleId);
    
    List<ArticleTag> findArticleTagsByTagId(@Param("tagId") Long tagId);
}