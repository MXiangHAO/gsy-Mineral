package com.example.blog.service;

import com.aliyun.tea.*;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class MsgService {

    private static final String ACCESS_KEY_ID = "LTAI5tEPd9TRBQXPmrEFaZUz";
    private static final String ACCESS_KEY_SECRET = "G98UtW8NTnsmp8vJwlyaD8ZxWhEcNO";
    private static final String SIGN_NAME = "中国地质大学学生开发";
    private static final String TEMPLATE_CODE = "SMS_464375060";

    private com.aliyun.dysmsapi20170525.Client client;

    public MsgService() {
        try {
            com.aliyun.dysmsapi20170525.Client client = createClient(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            this.client = client;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateRandomCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Generate a random number between 1000 and 9999
        return String.valueOf(code);
    }

    public void sendMessage(String phoneNumber, String verificationCode) {
        try {
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest =
                    new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                            .setSignName(SIGN_NAME)
                            .setTemplateCode(TEMPLATE_CODE)
                            .setPhoneNumbers(phoneNumber)
                            .setTemplateParam("{\"code\":\"" + verificationCode + "\"}");

            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            System.out.println(error.getMessage());
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            System.out.println(error.getMessage());
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

}