package com.realworld.conduit.mapper;

import com.realworld.conduit.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    void insertComment(Comment comment);
    
    Comment findById(@Param("id") Long id);
    
    List<Comment> findByArticleSlug(@Param("articleSlug") String articleSlug);
    
    void deleteById(@Param("id") Long id);
    
    int countByArticleSlug(@Param("articleSlug") String articleSlug);
}