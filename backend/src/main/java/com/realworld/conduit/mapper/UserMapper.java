package com.realworld.conduit.mapper;

import com.realworld.conduit.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    void insertUser(User user);
    
    User findByEmail(@Param("email") String email);
    
    User findByUsername(@Param("username") String username);
    
    User findById(@Param("id") Long id);
    
    boolean existsByEmail(@Param("email") String email);
    
    boolean existsByUsername(@Param("username") String username);
    
    void updateUser(User user);
}