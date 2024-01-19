package com.example.blog.mapper;

import com.example.blog.pojo.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {

    // 插入一条记录
    void insertLike(Like like);

    // 根据likeId删除一条记录
    void deleteLikeById(int likeId);

    // 更新记录
    void updateLike(Like like);

    // 根据uId和bId查询记录
    Like getLikeByUidAndBid(@Param("userId") int userId, @Param("blogId") int blogId);

    // 根据uId和bId删除记录
    void deleteLikeByUidAndBid(int userId, int blogId);
}
