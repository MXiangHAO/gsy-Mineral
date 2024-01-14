package com.example.chapter_blog.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.Blog;
import com.example.chapter_blog.pojo.BlogItem;
import com.example.chapter_blog.pojo.BlogItemManager;
import com.example.chapter_blog.pojo.User;
import com.example.chapter_blog.pojo.UserManager;
import com.example.chapter_blog.util.Client;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class blogInfo extends Activity implements View.OnClickListener {
    // 在类中添加常量
    private Bitmap downLoadBitmap;
    private ImageView followButton,btnComment, btnCollect,btnLike,userAvatar,btnBack,messageButton;
    private TextView username, blogTitle;
    private LinearLayout blogLayout;
    private Blog currentBlog;
    private User currentUser;
    private BlogItem currentBlogItem;
    private boolean isLiked = false,isCollect=false,isFollow=false;
    private Map<String, String> filesMap;
    private int uId,bId,FUId;
    private static final int REQUEST_WRITE_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_info);

        btnBack = findViewById(R.id.btnBack);
        btnLike = findViewById(R.id.btnLike);
        btnComment = findViewById(R.id.btnComment);
        btnCollect = findViewById(R.id.btnCollect);
        userAvatar = findViewById(R.id.userAvatar);
        username = findViewById(R.id.username);
        blogTitle = findViewById(R.id.blogTitle);
        blogLayout = findViewById(R.id.BlogLayout);
        followButton = findViewById(R.id.followButton);
        messageButton = findViewById(R.id.messageButton);

        btnBack.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnCollect.setOnClickListener(this);
        followButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);

        currentBlogItem= BlogItemManager.getInstance().getCurrentBlogItem();
        currentUser= UserManager.getInstance().getCurrentUser();
        if(currentUser.getId()==currentBlogItem.getuId()){
            // 隐藏关注按钮
            followButton.setVisibility(View.GONE);
            // 或者隐藏私信按钮
            messageButton.setVisibility(View.GONE);
        }

        uId=currentUser.getId();
        bId=currentBlogItem.getbId();
        FUId=currentBlogItem.getuId();
        try {
            JsonNode isLCF= Client.GetMapping("/blog/findLikeCommentFollow/"+uId+"/"
                    +bId+"/"+FUId).get();
            isLiked=isLCF.get("isLike").asBoolean();
            isCollect=isLCF.get("isCollect").asBoolean();
            isFollow=isLCF.get("isFollow").asBoolean();
            if (!isLiked) {
                btnLike.setImageResource(R.drawable.like); // 切换回原始图片
            } else {
                btnLike.setImageResource(R.drawable.islike); // 切换到另一个图片
            }
            if(!isCollect){
                btnCollect.setImageResource(R.drawable.star);
            }else{
                btnCollect.setImageResource(R.drawable.iscollect);
            }
            if(!isFollow){
                followButton.setImageResource(R.drawable.follow);
            }else {
                followButton.setImageResource(R.drawable.isfollow);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 获取传递过来的Intent
        Intent intent = getIntent();
        Serializable serializableMap = intent.getSerializableExtra("filesData");


        // 将Serializable对象转换回Map
        filesMap = (Map<String, String>) serializableMap;


        //创建Blog类
        currentBlog = new Blog(currentBlogItem.getbId(), currentBlogItem.getuId(), currentBlogItem.getTitle(), currentBlogItem.getContent(), currentBlogItem.getDate());

        //设置博客标题
        blogTitle.setText(currentBlog.getTitle());
        username.setText(currentUser.getUsername());
        userAvatar.setImageBitmap(decodeBase64ToBitmap(currentBlogItem.getAvatar()));
        // 动态创建布局
        createDynamicLayout(currentBlog);
        if(filesMap!=null){
            for (String key : filesMap.keySet()) {
                // 在这里处理每个键值对的键
                String keyValue = key;
                addFilesTextView(keyValue);
            }
        }


    }
    private void createDynamicLayout(Blog blog) {
        try {
            // 将 commentJson 解析为 JSONObject
            JSONObject commentObject = new JSONObject(blog.getContent());

            // 获取 commentObject 的键集合
            Iterator<String> keys = commentObject.keys();

            // 使用 while 循环迭代键值对
            while (keys.hasNext()) {
                String key = keys.next();

                // 如果键以 "text" 开头，创建 TextView 模块
                if (key.startsWith("text")) {
                    String textValue = commentObject.getString(key);
                    addTextView(textValue);
                }

                // 如果键以 "image" 开头，创建 ImageView 模块
                if (key.startsWith("image")) {
                    String base64Image = commentObject.getString(key);
                    Bitmap imageBitmap = decodeBase64ToBitmap(base64Image);

                    if (imageBitmap != null) {
                        addImageView(imageBitmap);
                    }
                }
            }

            // 创建内容 TextView 模块
            String contentValue = commentObject.getString("content");
            addTextView(contentValue);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void onClick(View v) {
        int id = v.getId();
        boolean flag;
        // 处理其他 View 的点击事件（如果有的话）
        if (id == R.id.btnBack) {// 处理返回按钮点击事件
            finish();
        } else if (id == R.id.btnLike) {
            //TODO 处理点赞按钮点击事件
            isLiked = !isLiked;
            if (!isLiked) {
                //取消点赞
                try {
                    flag=Client.judgeIs("/blog/like/cancel/"+uId+"/"+bId).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    btnLike.setImageResource(R.drawable.like); // 切换回原始图片
                }else {
                    Toast.makeText(this, "取消点赞失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                //添加点赞
                try {
                    flag=Client.judgeIs("/blog/like/add/"+uId+"/"+bId).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    btnLike.setImageResource(R.drawable.islike); // 切换到另一个图片
                }else {
                    Toast.makeText(this, "点赞失败", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.btnComment) {
            //TODO 处理评论按钮点击事件
            // 创建一个Intent对象，指定当前Activity和目标Activity
            Intent intent = new Intent(blogInfo.this, commentActivity.class);

            // 如果有需要传递数据到评论界面，可以使用putExtra方法
             intent.putExtra("bId",currentBlog.getBId());

            // 启动目标Activity
            startActivity(intent);

        }
        else if (id == R.id.btnCollect) {
            //TODO 处理收藏按钮点击事件
            isCollect=!isCollect;
            if(!isCollect){
                try {
                    flag=Client.judgeIs("/blog/collect/cancel/"+uId+"/"+bId).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    btnCollect.setImageResource(R.drawable.star);
                }else {
                    Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                }
            }else{
                try {
                    flag=Client.judgeIs("/blog/collect/add/"+uId+"/"+bId).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    btnCollect.setImageResource(R.drawable.iscollect);
                }else {
                    Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(id==R.id.messageButton){
            //TODO 跳转到聊天
            Intent intent = new Intent(this, chat.class);
            intent.putExtra("userId", currentUser.getId()); // 将userId添加到Intent
            intent.putExtra("friendId",currentBlog.getUId()); // 将friendId添加到Intent
            startActivity(intent);

        } else if (id==R.id.userAvatar) {
            Intent intent = new Intent(blogInfo.this, otherUserInfoActivity.class);
            intent.putExtra("uId",currentBlogItem.getuId());
            startActivity(intent);
        } else if (id==R.id.followButton)
        {
            isFollow=!isFollow;
            if(!isFollow){
                try {
                    flag=Client.judgeIs("/blog/follow/cancel/"+uId+"/"+FUId).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    followButton.setImageResource(R.drawable.follow);
                }else {
                    Toast.makeText(this, "取消关注失败", Toast.LENGTH_SHORT).show();
                }
            }else {
                try {
                    flag=Client.judgeIs("/blog/follow/add/"+uId+"/"+FUId).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    followButton.setImageResource(R.drawable.isfollow);
                }else {
                    Toast.makeText(this, "关注失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void addFilesTextView(String text) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText(text);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 在长按事件中弹出对话框
                showDownloadDialog(text);
                return true; // 返回true表示消耗了长按事件，不再触发普通点击事件
            }
        });
        blogLayout.addView(textView);
    }
    private void showDownloadDialog(final String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("下载确认")
                .setMessage("是否要下载：" + text)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO 启动下载
                        String url=filesMap.get(text);

                        //Toast.makeText(blogInfo.this, "下载：" + text, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户取消下载，可以不做任何操作
                    }
                })
                .show();
    }
    private void addTextView(String text) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText(text);
        blogLayout.addView(textView);
    }
    private void addImageView(final Bitmap bitmap) {
        final ImageView imgView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imgView.setLayoutParams(params);
        imgView.setAdjustViewBounds(true);
        imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // 设置Tag或其他你需要的属性
        // imgView.setTag(path);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理点击事件
                // Toast.makeText(postBlog.this, "图片地址" + imgView.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 长按事件：下载图片至本地相册
                downLoadBitmap=bitmap;
                saveImageToGallery(bitmap);

                Toast.makeText(blogInfo.this, "图片已下载至本地相册", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int scaledHeight = (int) ((float) originalHeight / originalWidth * screenWidth);

        params.width = screenWidth;
        params.height = scaledHeight;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, scaledHeight, true);
        imgView.setImageBitmap(scaledBitmap);

        blogLayout.addView(imgView);
    }

    private void saveImageToGallery(Bitmap bitmap) {
        // 检查存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            return;
        }

        // 保存图片至本地相册
        String savedImagePath = saveBitmap(bitmap);

        if (savedImagePath != null) {
            // 通知相册刷新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(savedImagePath))));

            // 可以显示一个提示，或者进行其他操作
            Toast.makeText(this, "图片已保存至相册", Toast.LENGTH_SHORT).show();
        } else {
            // 保存失败的处理
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveBitmap(Bitmap bitmap) {
        String savedImagePath = null;

        // 首先，创建保存图片的目录
        String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "YourAppDirectoryName");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // 在目录下创建文件
        File imageFile = new File(storageDir, imageFileName);
        savedImagePath = imageFile.getAbsolutePath();

        try {
            // 将Bitmap保存到文件
            FileOutputStream fOut = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            savedImagePath = null;
        }

        return savedImagePath;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 如果用户同意了权限请求，重新保存图片
                saveImageToGallery(downLoadBitmap);
            } else {
                // 如果用户拒绝了权限请求，可以进行相应的提示或处理
                Toast.makeText(this, "权限被拒绝，无法保存图片", Toast.LENGTH_SHORT).show();
            }
        }
    }


}