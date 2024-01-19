package com.example.chapter_blog.Activity;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private static List<FileInfo> fileInfos;
    ;

    public void setFileInfos(List<FileInfo> fileInfos) { // 添加新方法以设置 FileInfo 列表
        this.fileInfos = fileInfos;
        notifyDataSetChanged(); // 通知数据集更改
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileInfo fileInfo = fileInfos.get(position);
        holder.fileNameTextView.setText(fileInfo.getFilename());
        holder.selectButton.setOnClickListener(v -> {
            // 创建一个 Map 来存储 filename 和 url
            Map<String, String> fileInfoMap = new HashMap<>();
            fileInfoMap.put("filename", fileInfo.getFilename());
            fileInfoMap.put("url", fileInfo.getUrl());
            // TODO: 将相关内容进行传递到另一个界面！！
//            // 创建 Intent 并启动 BlogActivity
//            Intent intent = new Intent(v.getContext(), BlogActivity.class);
//            intent.putExtra("fileInfoMap", (HashMap<String, String>) fileInfoMap);
//            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fileInfos != null ? fileInfos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fileNameTextView;
        public Button openUrlButton;
        public Button selectButton;
        public ViewHolder(View view) {
            super(view);
            fileNameTextView = view.findViewById(R.id.fileNameTextView);
            openUrlButton = view.findViewById(R.id.button_open_url);
            selectButton = view.findViewById(R.id.button_select);
            // 初始化超链接按钮并设置点击监听器
            // 初始化选择按钮并设置点击监听器
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 获取当前项的 FileInfo 对象
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        FileInfo fileInfo = fileInfos.get(position);
                        // 提取 filename 和 url 并使用 Intent 传递到 BlogActivity
                        String filename = fileInfo.getFilename();
                        String url = fileInfo.getUrl();
//                        Intent intent = new Intent(v.getContext(), BlogActivity.class);
//                        intent.putExtra("filename", filename);
//                        intent.putExtra("url", url);
//                        v.getContext().startActivity(intent);
                    }
                }
            });

            openUrlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里获取当前项的 FileInfo 对象
                    FileInfo fileInfo = fileInfos.get(getAdapterPosition());
                    // 提取 URL 并使用 Intent 打开浏览器
                    String url = fileInfo.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}