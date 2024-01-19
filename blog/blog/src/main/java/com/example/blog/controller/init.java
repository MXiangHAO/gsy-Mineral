package com.example.blog.controller;
import com.example.blog.service.*;
import com.example.blog.pojo.*;
import com.example.blog.util.random;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/init")  // 替换为你实际想要的基本路径
public class init {
    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<String> initLogin(@RequestBody String jsonData) throws IOException {
        System.out.println("接收到的JSON数据：" + jsonData);
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        // 获取 type 字段的值
        String type = jsonNode.get("type").asText();
        String username=jsonNode.get("username").asText();
        String password=jsonNode.get("password").asText();
        User user=userService.getUserByType(username,type);
        String status;
        status= user != null && user.getPassword().equals(password)?"true":"false";
        // 构建 JSON 对象
        ObjectNode responseObject = objectMapper.createObjectNode();
        responseObject.put("status", status);
        if(user!=null){
            String userJson=user.toString();
            responseObject.put("user",userJson);
        }
        responseObject.put("action","login");
        responseObject.put("id",String.valueOf(user.getId()));
        responseObject.put("username",String.valueOf(user.getUsername()));
        responseObject.put("password",String.valueOf(user.getPassword()));
        responseObject.put("phone",String.valueOf(user.getPhone()));
        responseObject.put("email",user.getEmail());
        JsonNode userNode = responseObject.get("user");
        String userJson = userNode.toString();
        //user uJson = objectMapper.readValue(userJson, com.example.blog.pojo.user.class);
        // 返回响应，可以根据实际情况设置适当的 HTTP 状态码
        return new ResponseEntity<>(objectMapper.writeValueAsString(responseObject), HttpStatus.OK);
    }
    @GetMapping("/getUsername/{uId}")
    public ResponseEntity<String> getNewMessagesByOwnId(@PathVariable int uId){
        User user=userService.getUserById(uId);
        return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
    }
    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
    @PostMapping("/sendCode")
    public ResponseEntity<String> sendCode(@RequestBody String data) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(data);
        ObjectNode responseObject = objectMapper.createObjectNode();
        String  status;
        // 获取 type 字段的值
        String type = jsonNode.get("type").asText();
        String username=jsonNode.get("username").asText();
        User user=userService.getUserByType(username,type);
        status= user != null ?"true":"false";
        MsgService msgService=new MsgService();
        String code=msgService.generateRandomCode();

        if("false".equals(status)){
            //TODO: 不存在则发送验证码
            if("phone".equals(type)){

                msgService.sendMessage(username,code);
            }else {
                //TODO 发送邮箱
                String content="Mineral论坛感谢您的注册！您的验证码为："+code+"，请注意查收~";
                emailService.sendEmail(username, "【Mineral】注册验证码", content);
            }
        }
        responseObject.put("action","sendCode");
        responseObject.put("status","true");
        responseObject.put("code",code);
        return new ResponseEntity<>(objectMapper.writeValueAsString(responseObject), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> initRegister(@RequestBody String data) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(data);
        ObjectNode responseObject = objectMapper.createObjectNode();
        String type=jsonNode.get("type").asText();
        String username=jsonNode.get("username").asText();
        String password=jsonNode.get("password").asText();
        User user=new User();
        if ("phone".equals(type)){
            user.setPhone(username);
        }else{
            user.setEmail(username);
        }
        user.setPassword(password);
        String status;
        status= userService.addUser(user) ?"true":"false";
        responseObject.put("status",status);
        responseObject.put("action","register");
        return new ResponseEntity<>(objectMapper.writeValueAsString(responseObject), HttpStatus.OK);
    }


    @PostMapping("/loginForget")
    public ResponseEntity<String> initLoginForget(@RequestBody String data) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(data);
        ObjectNode responseObject = objectMapper.createObjectNode();
        String type=jsonNode.get("type").asText();
        String username=jsonNode.get("username").asText();
        String password=jsonNode.get("password").asText();
        User user=userService.getUserByType(username,type);
        String status;
        status= user != null ?"true":"false";
        responseObject.put("action","login_forget");
        if(user!=null){
            user.setPassword(password);
            if(userService.updateUser(user)){
                status="true";
            }else {
                status="false";
            }
        }
        responseObject.put("status",status);
        return new ResponseEntity<>(objectMapper.writeValueAsString(responseObject), HttpStatus.OK);
    }
}
