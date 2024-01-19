package com.example.chapter_blog;

import com.example.chapter_blog.pojo.Message;
import com.example.chapter_blog.pojo.MessageItem;
import com.example.chapter_blog.util.Client;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessageService {
    public static List<Message> downLoadMessages(String endpoint) throws ExecutionException, InterruptedException {
        List<Message> messageList= Client.parseJsonToMessages(Client.GetMapping(endpoint).get());
        return messageList;
    }

    public static List<MessageItem> getMessageItem(String endPoint) throws ExecutionException, InterruptedException {
        List<MessageItem> messageItemList=Client.parseJsonToMessagesItem(Client.GetMapping(endPoint).get());
        return  messageItemList;
    }
}
