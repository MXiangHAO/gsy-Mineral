package com.example.blog.mapper;

import com.example.blog.pojo.Friend;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendMapper {
    @Insert("INSERT INTO friend (userId, friendId) VALUES (#{userId}, #{friendId})")
    void addFriend(@Param("userId") int userId, @Param("friendId") int friendId);

    @Select("SELECT * FROM friend WHERE userId = #{userId}")
    List<Friend> findFriendByUserAndFriendId(@Param("userId") int userId);
}
