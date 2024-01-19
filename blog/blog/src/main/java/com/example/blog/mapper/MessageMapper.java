package com.example.blog.mapper;

import com.example.blog.pojo.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("INSERT INTO Message (friendId, ownId, content, timestamp, send, isRead) " +
            "VALUES (#{friendId}, #{ownId}, #{content}, #{timestamp}, #{send}, #{isRead})")
    int addMessage(Message message);

    @Select("SELECT * FROM Message WHERE (ownId = #{ownId} AND friendId = #{friendId})")
    List<Message> getMessagesByOwnIdAndFriendId(@Param("ownId") int ownId, @Param("friendId") int friendId);

    @Update("UPDATE Message SET isRead = true WHERE id = #{id}")
    void updateIsReadStatus(Message message);

    @Select("SELECT * FROM Message WHERE ownId = #{ownId} AND friendId = #{friendId} AND isRead='false'")
    List<Message> getNewMessagesByOwnIdAndFriendId(@Param("ownId")int ownId,  @Param("friendId")int friendId);


}
