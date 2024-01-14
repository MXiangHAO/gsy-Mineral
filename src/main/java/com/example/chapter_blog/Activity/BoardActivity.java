package com.example.chapter_blog.Activity;

import static com.example.chapter_blog.util.ApiUrls.BLOG_ITEM_CLICKS_UPDATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.BlogItem;
import com.example.chapter_blog.pojo.BlogItemManager;
import com.example.chapter_blog.util.ApiUrls;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
这是展示博客列表的Activity页面
 */

// 定义Android应用程序界面类
public class BoardActivity extends Activity implements View.OnClickListener{

    // 声明控件
    private EditText searchContext;
    private Button searchButton;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView recommendTextView;
    private TextView hotTextView;


    //初始化创建适配器并更新数据
    private BlogListAdapter adapter;

    private boolean listflag = true; // 默认：1为（recommend），0为（hot）初始为true表示默认recommend

    // 在Activity创建时调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //创建页面，绑定布局文件
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        //初始化页面
        initViews();
        //设置点击响应逻辑
        setClickListeners();
        //加载数据
        loadData();

    }

    // 初始化视图组件
    private void initViews(){
        //绑定控件
        searchContext = findViewById(R.id.search_context);
        searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.blog_list);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recommendTextView = findViewById(R.id.recommend);
        hotTextView = findViewById(R.id.hot);

        recommendTextView.setTypeface(null, Typeface.BOLD); // 加粗
        SpannableString content = new SpannableString(recommendTextView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        recommendTextView.setText(content);


        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener(this::onSwipeRefresh);
        //加载好数据前，进行圆圈等待
        swipeRefreshLayout.setRefreshing(true);
        adapter = new BlogListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 设置RecyclerView的布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // 设置博客之间的分割线
        DividerItemDecoration mDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDivider);

        /*******底部导航栏*********/
        BottomNavigationView bottomNavigationView=findViewById(R.id.botton_navigation);//定位底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.home);//默认选择主页
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), BoardActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.library) {
                    startActivity(new Intent(getApplicationContext(), TargetActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.message) {
                    startActivity(new Intent(getApplicationContext(), message.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId==R.id.add) {
                    startActivity(new Intent(getApplicationContext(), postBlog.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.mine) {
                    startActivity(new Intent(getApplicationContext(), firstUserInfoActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
    private void onSwipeRefresh() {
        // 显示加载动画
        swipeRefreshLayout.setRefreshing(true);
        // 获取传递过来的 Intent
        Intent intent = getIntent();
        // 检查是否从收藏夹跳转的博客
        if (intent.hasExtra("cid")) {
            // 获取 "cid" 键对应的值
            int cid = intent.getIntExtra("cid", -1); // -1 是默认值，如果获取失败则返回 -1
            if (cid != -1) {
                fetchBlogFromCollect(cid);
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
        }
        // 如果没有从其他页面跳转返回的华，则默认为刷新所有博客
        fetchDataAndDisplay(ApiUrls.BLOG_ITEM_ALL);
    }

    // 设置按钮点击监听器
    private void setClickListeners() {
        // 设置搜索按钮的点击事件监听器
        searchButton.setOnClickListener(this::onClick);
        recommendTextView.setOnClickListener(this::onClick);
        hotTextView.setOnClickListener(this::onClick);
    }

    // 按钮点击事件处理
    @Override
    public void onClick(View view) {
        // 根据点击的按钮执行相应的操作
        if (view.getId() == R.id.search_button) {
            String searchKeyword = searchContext.getText().toString();
            swipeRefreshLayout.setRefreshing(true);
            performSearch(searchKeyword);
        }
        else if (view.getId() == R.id.recommend) {
            listflag = true; // 设置为推荐
            hotTextView.setTypeface(null, Typeface.NORMAL); // 取消加粗
            recommendTextView.setTypeface(null, Typeface.BOLD); // 加粗

            SpannableString content = new SpannableString(recommendTextView.getText());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            recommendTextView.setText(content);

            // 移除 hot 的下划线
            SpannableString hotContent = new SpannableString(hotTextView.getText());
            // 查找并移除所有的UnderlineSpan
            UnderlineSpan[] underlineSpans = hotContent.getSpans(0, hotContent.length(), UnderlineSpan.class);
            for (UnderlineSpan span : underlineSpans) {
                hotContent.removeSpan(span);
            }
            hotTextView.setText(hotContent);

            swipeRefreshLayout.setRefreshing(true);
            loadData();
        } else if (view.getId() == R.id.hot) {
            listflag = false; // 设置为热榜
            recommendTextView.setTypeface(null, Typeface.NORMAL); // 取消加粗
            hotTextView.setTypeface(null, Typeface.BOLD); // 加粗

            SpannableString content = new SpannableString(hotTextView.getText());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            hotTextView.setText(content);

            // 移除 recommend 的下划线
            SpannableString recommendContent = new SpannableString(recommendTextView.getText());
            // 查找并移除所有的UnderlineSpan
            UnderlineSpan[] underlineSpans = recommendContent.getSpans(0, recommendContent.length(), UnderlineSpan.class);
            for (UnderlineSpan span : underlineSpans) {
                recommendContent.removeSpan(span);
            }
            recommendTextView.setText(recommendContent);

            swipeRefreshLayout.setRefreshing(true);
            fetchBlogFromRank();
        }
    }

    // 解析搜索结果JSON数据
    private List<BlogItem> parseSearchResult(String jsonData) {
        // 解析JSON数据并创建BlogItem对象列表
        List<BlogItem> searchResult = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(jsonData);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                // 解析数据并创建 BlogItem 对象
                int bId = jsonObject.getInt("bid");
                int uId = jsonObject.getInt("uid");
                String urls=jsonObject.getString("urls");

                String title = jsonObject.getString("title");
                String date = jsonObject.getString("date");
                String content = jsonObject.getString("content");
                String fullName = jsonObject.getString("fullName");
                String avatar = jsonObject.getString("avatar");
                int clicks = jsonObject.getInt("clicks");
                int likes = jsonObject.getInt("likes");
                int collects = jsonObject.getInt("collects");

                BlogItem blogItem = new BlogItem(bId,uId,urls,title,content, date,fullName,avatar,clicks,likes,collects);
                searchResult.add(blogItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    // 将URL字符串分割成url列表
    public  List<String> splitUrls(String urls) {
        List<String> urlList = new ArrayList<>();
        if (urls != null && !urls.isEmpty()) {
            String[] urlArray = urls.split(",");
            for (String url : urlArray) {
                urlList.add(url.trim());
            }
        }
        return urlList;
    }
    // 将URL列表映射到文件名
    public Map<String, String> mapFileNames(List<String> urlList) {
        Map<String, String> fileNameMap = new HashMap<>();
        for (String url : urlList) {
            String[] parts = url.split("/");
            if (parts.length > 0) {
                String fileName = parts[parts.length - 1];
                fileNameMap.put(url, fileName);
            }
        }
        return fileNameMap;
    }

    // 加载数据
    private void loadData() {
        // 先获取传递过来的 Intent
        Intent intent = getIntent();
        // 检查跳转页面后对应的数据
        //从收藏夹中跳转到博客页面，获取上文的cid（收藏夹id）
        if (intent.hasExtra("cid")) {
            // 解析获取 "cid" 键对应的值
            int cid = intent.getIntExtra("cid", -1); // -1 是默认值，如果获取失败则返回 -1
            fetchBlogFromCollect(cid);
        } else {
            fetchDataAndDisplay(ApiUrls.BLOG_ITEM_ALL);
        }
    }

    //获取数据在后台进行，避免堵塞主进程
    private void fetchDataAndDisplayInBackground(String url) {
        // 使用OkHttpClient进行网络请求并处理响应
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理错误情况
                e.printStackTrace();
                // 隐藏加载动画
                swipeRefreshLayout.setRefreshing(false);
                showToast("Failed to fetch data"); // 显示加载失败的Toast
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String jsonData = response.body().string();
                List<BlogItem> result = parseSearchResult(jsonData);
                runOnUiThread(() -> {
                    adapter.setData(result); // 更新适配器中的数据
                    // 隐藏加载动画
                    swipeRefreshLayout.setRefreshing(false);
                    showToast("Data fetched successfully"); // 显示加载成功的Toast
                });
            }
        });
    }

    private void updateBlogClicks(int bId) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = BLOG_ITEM_CLICKS_UPDATE + "?bId=" + bId;
        Request request = new Request.Builder()
                .url(url) // 将bId和clicks作为URL的一部分
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理错误情况
                e.printStackTrace();
                showToast("Failed to update blog"); // 显示更新失败的提示
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String responseData = response.body().string();
                // 处理更新成功的响应
                runOnUiThread(() -> {
                    // 刷新数据等操作
                    showToast("Blog updated successfully"); // 显示更新成功的提示
                });
            }
        });
    }

    //显示toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //展示博客
    private void fetchDataAndDisplay(String url) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            fetchDataAndDisplayInBackground(url);
        });
    }
    private void fetchBlogAll() {
        String url = ApiUrls.BLOG_ITEM_ALL;
        fetchDataAndDisplay(url);
    }
    //展示根据关键词搜索到的博客
    private void performSearch(String keyword) {
        String url = ApiUrls.BLOG_ITEM_SEARCH + "?keyword=" + keyword;
        fetchDataAndDisplay(url);
    }
    //展示收藏夹中的博客
    private void fetchBlogFromCollect(int cid) {
        String url = ApiUrls.BLOG_ITEM_COLLECT + "?cid=" + cid;
        fetchDataAndDisplay(url);
    }
    private void fetchBlogFromRank() {
        String url = ApiUrls.BLOG_ITEM_RANK;
        fetchDataAndDisplay(url);
    }

    // 适配器类用于RecyclerView，进行博客BlogItem的List的装载与空间展示
    public class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.ViewHolder> {
        //成员变量，装载数据
        private List<BlogItem> mData;
        // 构造方法，接收数据列表
        public BlogListAdapter(List<BlogItem> data) {
            this.mData = data;
        }
        //更新适配器
        public void setData(List<BlogItem> newData) {
            mData.clear();
            mData.addAll(newData);
            notifyDataSetChanged();
        }
        // 创建ViewHolder
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
            return new ViewHolder(view);
        }

        // 绑定ViewHolder，设置数据
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            BlogItem blogItem = mData.get(position);


            // 在这里设置ViewHolder中的控件数据，例如：
            holder.title.setText(blogItem.getTitle());
            holder.fullName.setText(blogItem.getFullName());
            holder.date.setText(blogItem.getDate());
            int clicks = blogItem.getClicks();
            int likes = blogItem.getLikes();
            int collects = blogItem.getCollects();
            String displayClicks = clicks + "阅读";
            String displayLikes = likes + "点赞";
            String displayCollects = collects + "收藏";
            holder.clicks.setText(displayClicks);
            holder.likes.setText(displayLikes);
            holder.collects.setText(displayCollects);

            // 获取Base64字符串
            String base64Image = blogItem.getAvatar();
            // 将Base64字符串转换为字节数组
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            // 将字节数组转换为位图
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.avatar.setImageBitmap(decodedByte);
            if(!listflag){
                String rank= String.valueOf(position+1);
                holder.rank.setText(rank);
            }
            else{
                holder.rank.setText("");}
            // 设置点击事件
            holder.itemView.setOnClickListener(view -> {
                updateBlogClicks(blogItem.getbId());
                BlogItemManager.getInstance().setCurrentBlogItem(blogItem);
                // 创建一个 Intent  跳转这里要改一下
                Intent intent = new Intent(view.getContext(), blogInfo.class);
                // 启动新的 Activity，并传递 Intent
                view.getContext().startActivity(intent);
            });
        }

        // 获取数据列表的大小
        @Override
        public int getItemCount() {
            return mData.size();
        }

        // ViewHolder类，用于保存每个列表项的控件引用
        public class ViewHolder extends RecyclerView.ViewHolder {
            // 在这里声明你的控件，例如：
            TextView title;
            TextView date;
            TextView fullName;
            ImageView avatar;
            TextView clicks;
            TextView likes;
            TextView collects;
            TextView rank;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // 在这里初始化控件，例如：
                title = itemView.findViewById(R.id.title);
                date=itemView.findViewById(R.id.date);
                fullName=itemView.findViewById(R.id.fullname);
                avatar=itemView.findViewById(R.id.avatar);
                clicks=itemView.findViewById(R.id.clicks);
                likes=itemView.findViewById(R.id.likes);
                collects=itemView.findViewById(R.id.collects);
                rank=itemView.findViewById(R.id.ranking_number);
            }
        }
    }
}