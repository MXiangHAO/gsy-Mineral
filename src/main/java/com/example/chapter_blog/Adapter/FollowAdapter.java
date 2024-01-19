package com.example.chapter_blog.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.UserDetailAdduId;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private List<UserDetailAdduId> followList; // 关注用户的列表
    private OnUserClickListener listener; // 点击监听器

    // Update constructor to accept the listener
    public FollowAdapter(List<UserDetailAdduId> followList, OnUserClickListener listener) {
        this.followList = followList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following, parent, false);
        return new ViewHolder(view, listener); // Pass the listener to the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserDetailAdduId user = followList.get(position);
        holder.username.setText(user.getUsername()); // 显示用户名
        try {
            holder.avatar.setImageBitmap(getAvatarBitmapFromBase64(user.getAvatar())); // 显示头像
        } catch (IllegalArgumentException e) {
            Log.e("FollowAdapter", "Error decoding avatar image", e);
        }
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView avatar;

        ViewHolder(View itemView, final OnUserClickListener listener) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            avatar = itemView.findViewById(R.id.avatar);

            // Set the click listener for the whole item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onUserClick(position); // Pass the position or any other data needed
                        }
                    }
                }
            });
        }
    }

    // 解析Base64编码的头像为Bitmap
    private Bitmap getAvatarBitmapFromBase64(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public interface OnUserClickListener {
        void onUserClick(int position);  // 或者传递更多信息，取决于用户主页需要什么数据
    }
}
