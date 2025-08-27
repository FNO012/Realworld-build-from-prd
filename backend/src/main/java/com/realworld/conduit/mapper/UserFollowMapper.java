package com.realworld.conduit.mapper;

import com.realworld.conduit.model.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowMapper {
    
    // 팔로우 관계 생성
    void insertFollow(UserFollow userFollow);
    
    // 팔로우 관계 삭제
    void deleteFollow(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    // 팔로우 여부 확인
    boolean isFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    // 특정 사용자를 팔로우하는 사용자 목록
    List<UserFollow> findFollowersByUserId(@Param("userId") Long userId);
    
    // 특정 사용자가 팔로우하는 사용자 목록
    List<UserFollow> findFollowingByUserId(@Param("userId") Long userId);
    
    // 팔로워 수 조회
    int countFollowers(@Param("userId") Long userId);
    
    // 팔로잉 수 조회
    int countFollowing(@Param("userId") Long userId);
}