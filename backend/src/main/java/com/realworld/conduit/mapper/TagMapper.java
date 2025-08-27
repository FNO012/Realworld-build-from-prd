package com.realworld.conduit.mapper;

import com.realworld.conduit.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    
    void insertTag(Tag tag);
    
    Tag findByName(@Param("name") String name);
    
    List<Tag> findAllTags();
    
    List<Tag> findTagsByArticleId(@Param("articleId") Long articleId);
}