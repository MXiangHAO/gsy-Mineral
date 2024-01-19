package com.example.blog.controller;

import com.example.blog.pojo.*;
import com.example.blog.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author KGong
 */
@RestController
@RequestMapping("/blog")
public class BlogOkhttp {
    @Autowired
    private final BlogService blogService;
    @Autowired
    private  final FileService fileService;
    @Autowired
    private  final LikeService likeService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private  final CollectService collectService;
    @Autowired
    private final FollowService followService;


    public BlogOkhttp(BlogService blogService, FileService fileService,LikeService likeService,CollectService collectService,FollowService followService) {
        this.blogService = blogService;
        this.fileService = fileService;
        this.likeService=likeService;
        this.collectService=collectService;
        this.followService=followService;
    }

    @GetMapping("/findLikeCommentFollow/{uId}/{bId}/{f_uId}")
    public ResponseEntity<String> findLikeCommentFollow(@PathVariable int uId, @PathVariable int bId,@PathVariable int f_uId) throws JsonProcessingException {
        Like like=likeService.getLikeByUidAndBid(uId,bId);
        System.out.println(likeService.getLikeByUidAndBid(uId,bId));
        boolean isLike=false;
        if(like!=null){
            isLike=true;
        }
        boolean isCollect=false;
        Collect collect=collectService.getCollectByUidAndBid(uId,bId);
        if(collect!=null){
            isCollect=true;
        }
        boolean isFollow=false;
        Follow follow=followService.getFollowByUidAndFUid(uId, f_uId);
        if(follow!=null){
            isFollow=true;
        }
        ObjectNode responseObject=objectMapper.createObjectNode();
        responseObject.put("isLike",isLike);
        responseObject.put("isCollect",isCollect);
        responseObject.put("isFollow",isFollow);
        return new ResponseEntity<>(objectMapper.writeValueAsString(responseObject), HttpStatus.OK);

    }

    @GetMapping("/follow/add/{uId}/{fUId}")
    public ResponseEntity<Boolean> addFollow(@PathVariable("uId") int uId, @PathVariable("fUId") int fUId) {
        boolean success = followService.followUser(uId, fUId) > 0;
        return new ResponseEntity<>(success, success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/follow/cancel/{uId}/{fUId}")
    public ResponseEntity<Boolean> cancelFollow(@PathVariable("uId") int uId, @PathVariable("fUId") int fUId) {
        boolean success = followService.unfollowUser(uId, fUId) > 0;
        return new ResponseEntity<>(success, success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/like/add/{uId}/{bId}")
    public ResponseEntity<Boolean> addLike(@PathVariable("uId") int uId, @PathVariable("bId") int bId) {
        Like like = new Like(uId, bId);
        likeService.addLike(like);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/like/cancel/{uId}/{bId}")
    public ResponseEntity<Boolean> cancelLike(@PathVariable("uId") int uId, @PathVariable("bId") int bId) {
        likeService.deleteLikeByUidAndBid(uId, bId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/collect/add/{uId}/{bId}")
    public ResponseEntity<Boolean> addCollect(@PathVariable("uId") int uId, @PathVariable("bId") int bId) {
        Collect collect = new Collect(uId, bId);
        collectService.addCollect(collect);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/collect/cancel/{uId}/{bId}")
    public ResponseEntity<Boolean> cancelCollect(@PathVariable("uId") int uId, @PathVariable("bId") int bId) {
        collectService.deleteCollectByUidAndBid(uId, bId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBlog(@RequestBody String jsonBody) throws JsonProcessingException {
        ObjectNode responseObject = objectMapper.createObjectNode();
        String status="false";
        try {
            // Convert the BlogRequest to your Blog entity
            Blog blog = convertToBlog(jsonBody);
            //File file=convertToFile(jsonBody,blogService.getBlogIdByUD(blog.getUId(),blog.getDate()));
            // Call the service method to add the blog
            if(blogService.addBlog(blog)&&addFiles(jsonBody,blogService.getBlogIdByUD(blog.getUId(),blog.getDate()),blog.getUId())){
                status = "true";
            }
            System.out.println("已收到");
            // Return a success response
            responseObject.put("status",status);
            return new ResponseEntity<>(objectMapper.writeValueAsString(responseObject), HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception (you might want to log more details)
            e.printStackTrace();
            // Return an error response
            return new ResponseEntity<>(objectMapper.writeValueAsString(responseObject), HttpStatus.OK);
        }
    }
    private Blog convertToBlog(String json) {
        // Convert the JSON string to JsonObject
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        int uId = jsonObject.has("userId") && !jsonObject.get("userId").isJsonNull() ? jsonObject.get("userId").getAsInt() : null;
        String title = jsonObject.has("title") && !jsonObject.get("title").isJsonNull() ? jsonObject.get("title").getAsString() : null;
        String content = jsonObject.has("body") && !jsonObject.get("body").isJsonNull() ? jsonObject.getAsJsonObject("body").toString() : null;
        String date = jsonObject.has("date") && !jsonObject.get("date").isJsonNull() ? jsonObject.get("date").getAsString() : null;


        // Create a new Blog instance
        return new Blog(uId, title, content, date);
    }
    private boolean addFiles(String json,int bId,int uId) {

        // Convert the JSON string to JsonObject
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject filesObject = jsonObject.getAsJsonObject("files");
        if (filesObject == null || filesObject.isJsonNull() || filesObject.size() == 0) {
            return true;
        } else {
            for (String fileKey : filesObject.keySet()) {
                String url = filesObject.get(fileKey).getAsString();
                if(fileService.addFile(new File(uId, url, bId))<1){
                    return false;
                }
            }
            return  true;
        }
    }

}
