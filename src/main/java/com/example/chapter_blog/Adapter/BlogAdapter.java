package com.example.chapter_blog.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.Blog;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private final List<Blog> blogs; // 博客数据
    private BlogItemDeleteListener deleteListener; // 删除监听器
    private static final String TAG = "BlogAdapter";
    //Todo: 添加一个成员变量用于监听器
    private OnItemClickListener listener; //条目监听器
    // 新增成员变量
    private boolean showDeleteButton;

    //Todo: 修改构造函数来接受这个监听器
    public BlogAdapter(List<Blog> blogs, BlogItemDeleteListener deleteListener, OnItemClickListener listener, boolean showDeleteButton) {
        this.blogs = blogs;
        this.deleteListener = deleteListener;
        this.listener = listener;
        this.showDeleteButton = showDeleteButton; // 接收是否显示删除按钮的标志
    }

    // 构造器
    public BlogAdapter(List<Blog> blogs, BlogItemDeleteListener deleteListener) {
        this.blogs = blogs;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载博客项布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_post_item, parent, false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog = blogs.get(position);
        // 设置博客项的数据
        holder.title.setText(blog.getTitle());
        holder.date.setText(blog.getDate());

        // 为删除按钮设置点击事件监听器
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(blogs.get(position).getBId());
            }
        });

        // 为整个条目设置点击事件监听器
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(blog.getBId());
            }
        });
        // 根据showDeleteButton的值来设置删除按钮的可见性
        holder.deleteButton.setVisibility(showDeleteButton ? View.VISIBLE : View.GONE);

    }


    @Override
    public int getItemCount() {
        return blogs.size();
    }
///////------888888999999555555//////
    // ViewHolder内部类
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title; // 博客标题
        TextView date; // 博客日期
        ImageButton deleteButton; // 删除按钮

    ViewHolder(View itemView, final BlogAdapter.OnItemClickListener listener) {
        super(itemView);
        title = itemView.findViewById(R.id.blogTitle);
        date = itemView.findViewById(R.id.blogDate);
        deleteButton = itemView.findViewById(R.id.deleteButton); // 绑定删除按钮

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(blogs.get(position).getBId());
                Log.d(TAG, "Blog item clicked: " + blogs.get(position).getBId());
            }
        });
    }
    }

    // Todo：删除监听器接口
    public interface BlogItemDeleteListener {
        void onDelete(int bId);
    }

    //Todo：点击条目的监听器接口
    public interface OnItemClickListener {
        void onItemClick(int bId);
    }

}
