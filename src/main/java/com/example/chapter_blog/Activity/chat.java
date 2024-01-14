package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chapter_blog.MessageService;
import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.Message;
import com.example.chapter_blog.util.Client;
import com.example.chapter_blog.util.DateUtil;
import com.example.chapter_blog.util.NetworkUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class chat extends Activity implements View.OnClickListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private LinearLayout linearLayoutMessages;
    private EditText editTextMessage;
    private TextView friendName;
    private NetworkUtil networkUtil = new NetworkUtil();
    private Button btn_send;
    private ImageView btn_back;
    private int userId,friendId;
    private List<Message>messageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取传递过来的值
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1); // defaultValue是userId的默认值
        friendId = intent.getIntExtra("friendId",-1); // defaultValue是friendId的默认值

        linearLayoutMessages = findViewById(R.id.linearLayoutMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        btn_send = findViewById(R.id.buttonSend);
        btn_back=findViewById(R.id.btn_back);
        friendName=findViewById(R.id.textViewFriendName);
        try {
            String firendName= Client.GetUsername("/init/getUsername/"+friendId).get();
            friendName.setText(firendName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            messageList=MessageService.downLoadMessages("/message/getMessages/"+userId+"/"+friendId);
            initChat(messageList);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        btn_send.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        getNewMessageNowIntime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduler.shutdown();
    }

    public void getNewMessageNowIntime(){
        // 在适当的地方启动定时任务
        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<Message> messages = Client.parseJsonToMessages(Client.GetMapping("/message/getNewMessages/" + userId + "/" + friendId).get());
                for (Message message : messages) {
                    creatLayout(message.getContent(), false);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
    public void creatLayout(String msg,boolean ...isUser){
        boolean isUserMessage = isUser.length > 0 ? isUser[0] : true;
        // 创建消息的布局
        RelativeLayout messageLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams messageLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        messageLayoutParams.setMargins(0, 0, 0, 16); // 设置底部间距，可以根据需要调整
        messageLayout.setLayoutParams(messageLayoutParams);
        messageLayout.setGravity(isUserMessage ? Gravity.END : Gravity.START);

        // 创建消息的 TextView
        TextView textViewMessage = new TextView(this);
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textViewParams.setMargins(0, 0, 0, 8); // 设置底部间距，可以根据需要调整
        textViewMessage.setLayoutParams(textViewParams);
        textViewMessage.setBackgroundResource(isUserMessage ? R.color.userMessageBackground : R.color.friendMessageBackground);
        textViewMessage.setPadding(10, 10, 10, 10);
        textViewMessage.setTextColor(Color.WHITE);
        textViewMessage.setText(msg);

        // 将 TextView 添加到消息布局
        messageLayout.addView(textViewMessage);

        // 将消息布局添加到线性布局
        linearLayoutMessages.addView(messageLayout);
    }
    public void initChat(List<Message> messageList){
        for (Message message : messageList) {
            boolean isUserMessage = message.getOwnId() == userId;
            creatLayout(message.getContent(),isUserMessage);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.buttonSend){
            Message message=new Message(friendId,userId,editTextMessage.getText().toString(), DateUtil.getNowTime(),0,false);
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson="";
            try {
                 messageJson=objectMapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Client.sendPost("/message/add",messageJson);
            // 创建消息的布局
            creatLayout(message.getContent());
            editTextMessage.setText("");
        }else if(id==R.id.btn_back){
            finish();
        }
    }
}
