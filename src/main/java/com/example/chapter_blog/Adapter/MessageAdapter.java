package com.example.chapter_blog.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.Activity.chat;
import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.MessageItem;
import com.example.chapter_blog.pojo.UserManager;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageItem> messageList;
    private Context context;

    public MessageAdapter(Context context, List<MessageItem> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageItem messageItem = messageList.get(position);
        holder.bind(messageItem);

        holder.itemView.setOnClickListener(view -> {
            // 创建一个 Intent  跳转这里要改一下
            int userId=UserManager.getInstance().getCurrentUser().getId();
            int friendId=UserManager.getInstance().getCurrentUser().getId()==messageItem.getReceiverId()?messageItem.getSenderId():messageItem.getReceiverId();
            Intent intent = new Intent(view.getContext(), chat.class);
            intent.putExtra("userId", userId);
            intent.putExtra("friendId",friendId);
            // 启动新的 Activity，并传递 Intent
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    public void updateData(List<MessageItem> newData) {
        messageList.clear();
        messageList.addAll(newData);
        notifyDataSetChanged();
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private TextView tvSenderId;
        private TextView tvReceiverId;
        private TextView tvTimestamp;
        private TextView tvRecentMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvRecentMessage=itemView.findViewById(R.id.RecentMessage);
        }

        public void bind(MessageItem messageItem) {
            ivUserAvatar.setImageBitmap(messageItem.getUserAvatar());
            tvUsername.setText(messageItem.getUsername());
            tvTimestamp.setText(messageItem.getTimestamp());
            tvRecentMessage.setText(messageItem.getRecentMessage());
        }
    }
}

