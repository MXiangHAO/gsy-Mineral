package com.example.chapter_blog.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.Comment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> comments; // 评论数据

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        // 设置用户名和头像，这里假设Comment对象已经包含了这些信息
        holder.username.setText(comment.getUsername());
        // 这里应该加载头像图片，比如使用Glide或Picasso库。这里只是简单地设置用户名
        // 例如: Glide.with(context).load(comment.getAvatar()).into(holder.avatar);

        holder.content.setText(comment.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        holder.date.setText(sdf.format(comment.getDate()));

        if (comment.getAvatar() != null && !comment.getAvatar().trim().isEmpty()) {
            byte[] decodedString = Base64.decode(comment.getAvatar(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.avatar.setImageBitmap(decodedByte);
        }

        // Set the username text
        holder.username.setText(comment.getUsername());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // 修改ViewHolder以包含用户名和头像视图
        TextView username; // 用户名
        ImageView avatar; // 头像
        TextView content; // 评论内容
        TextView date; // 评论日期

        ViewHolder(View itemView) {
            super(itemView);
            // 初始化视图引用
            username = itemView.findViewById(R.id.username);
            avatar = itemView.findViewById(R.id.avatar);
            content = itemView.findViewById(R.id.comment_content);
            date = itemView.findViewById(R.id.comment_date);
        }
    }
}
