package com.example.blog.mapper;

import com.example.blog.pojo.Follow;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FollowMapper {

    @Insert("INSERT INTO follow(uId, fUId) VALUES(#{uId}, #{FUId})")
    int insertFollow(Follow follow);


    @Delete("DELETE FROM follow WHERE uId = #{uId} AND FUId = #{FUId}")
    int deleteFollowByUidAndFUid(@Param("uId") int uId, @Param("FUId") int FUId);



    @Select("SELECT * FROM follow WHERE uId = #{uId} AND FUId = #{FUId}")
    Follow selectFollowByUidAndFUid(@Param("uId") int uId, @Param("FUId") int fUId);
}