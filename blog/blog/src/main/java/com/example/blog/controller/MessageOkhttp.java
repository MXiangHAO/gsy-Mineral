package com.example.blog.controller;
import com.example.blog.pojo.*;
import com.example.blog.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KGong
 */
@RestController
@RequestMapping("/message")
public class MessageOkhttp {
    private final MessageService messageService;
    private final FriendService friendService;
    private final UserInfoService userInfoService;


    @Autowired
    public MessageOkhttp(MessageService messageService,FriendService friendService,UserInfoService userInfoService) {
        this.messageService = messageService;
        this.friendService=friendService;
        this.userInfoService=userInfoService;
    }

    @GetMapping("/getMessages/{ownId}/{friendId}")
    @ResponseBody
    public ResponseEntity<List<Message>> getMessagesByOwnId(@PathVariable int ownId,@PathVariable int friendId) {
        List<Message> ownMessages = messageService.getMessagesByOwnIdAndFriendId(ownId,friendId);
        List<Message> friendMessages=messageService.getMessagesByOwnIdAndFriendId(friendId,ownId);
        List<Message> messages = new ArrayList<>(ownMessages);
        messages.addAll(friendMessages);
        // 根据消息的 Id 进行升序排序
        List<Message> sortedMessages = messages.stream()
                .sorted(Comparator.comparingInt(Message::getId))
                .collect(Collectors.toList());
        for(Message message:sortedMessages){
            message.setRead(true);
            messageService.updateIsReadStatus(message);
        }
        return new ResponseEntity<>(sortedMessages, HttpStatus.OK);
    }

    @GetMapping("/getNewMessages/{ownId}/{friendId}")
    public ResponseEntity<List<Message>> getNewMessagesByOwnId(@PathVariable int ownId,@PathVariable int friendId){
        List<Message>messageList=messageService.getNewMessagesByOwnIdAndFriendId(ownId,friendId);
        for(Message message:messageList){
            message.setRead(true);
            messageService.updateIsReadStatus(message);
        }
        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addMessage(@RequestBody Message message) {
        boolean success = messageService.addMessage(message) > 0;
        return new ResponseEntity<>(success, success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/addFriend/{userId}/{friendId}")
    public ResponseEntity<Boolean> addFriend(@PathVariable int userId, @PathVariable int friendId) {
        try {
            // 调用 FriendService 的添加好友方法
            friendService.addFriend(userId, friendId);
            // 返回成功的响应
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            // 如果添加好友失败，返回失败的响应
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findMessageItem/{userId}")
    public ResponseEntity<List<MessageItem>> findMessageItem(@PathVariable int userId) {
        try {
            // 调用 FriendService 的方法获取好友列表
            List<Friend> friendList = friendService.findFriendByUserId(userId);
            List<MessageItem>messageItemList=new ArrayList<>();
            for(Friend friend:friendList){
                UserInfo userInfo=userInfoService.getUserInfoByUserId(friend.getFriendId());
                List<Message> ownMessages = messageService.getMessagesByOwnIdAndFriendId(friend.getUserId(),friend.getFriendId());
                List<Message> friendMessages=messageService.getMessagesByOwnIdAndFriendId(friend.getFriendId(),friend.getUserId());
                List<Message> messages = new ArrayList<>(ownMessages);
                messages.addAll(friendMessages);
                // 根据消息的 Id 进行升序排序
                List<Message> sortedMessages = messages.stream()
                        .sorted(Comparator.comparingInt(Message::getId))
                        .collect(Collectors.toList());

                String content=sortedMessages.get(sortedMessages.size()-1).getContent();
                String time=sortedMessages.get(sortedMessages.size()-1).getTimestamp();
                User user=userInfoService.getUserById(userInfo.getUserId());
                MessageItem messageItem=new MessageItem(userInfo.getAvatar(),user.getUsername(),friend.getUserId(),friend.getFriendId(),time,content);
                messageItemList.add(messageItem);
            }
            // 降序排列
            List<MessageItem> sortedList = messageItemList.stream()
                    .sorted(Comparator.comparing(MessageItem::getTimestamp).reversed())
                    .collect(Collectors.toList());

            // 返回好友列表
            return new ResponseEntity<>(sortedList, HttpStatus.OK);
        } catch (Exception e) {
            // 如果查询失败，返回失败的响应
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
