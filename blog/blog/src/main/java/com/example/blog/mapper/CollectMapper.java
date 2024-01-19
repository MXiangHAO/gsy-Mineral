package com.example.blog.mapper;

import com.example.blog.pojo.Collect;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CollectMapper {

    @Insert("INSERT INTO collect(cId, uId, bId, collectName) VALUES(#{cId}, #{uId}, #{bId}, #{collectName})")
    int insertCollect(Collect collect);

    @Delete("DELETE FROM collect WHERE uId = #{uId} AND bId = #{bId}")
    int deleteCollectByUidAndBid(@Param("uId") int uId, @Param("bId") int bId);

    @Update("UPDATE collect SET cId = #{cId}, collectName = #{collectName} WHERE uId = #{uId} AND bId = #{bId}")
    int updateCollect(Collect collect);

    @Select("SELECT * FROM collect WHERE uId = #{uId} AND bId = #{bId}")
    Collect selectCollectByUidAndBid(@Param("uId") int uId, @Param("bId") int bId);
}

