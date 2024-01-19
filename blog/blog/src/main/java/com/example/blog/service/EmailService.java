package com.example.blog.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

@Service
public class EmailService {

    private static final String FROM_ADDRESS = "mineral@www.brayden.asia"; // 发件人邮箱
    private static final String SMTP_HOST = "smtpdm.aliyun.com";
    private static final String SMTP_PORT = "25"; // 根据阿里云邮件服务的要求更新端口
    private static final String USERNAME = "mineral@www.brayden.asia";
    private static final String PASSWORD = "WUzp020905";
    private Session session;

    public EmailService() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", SMTP_HOST);
        properties.setProperty("mail.smtp.port", SMTP_PORT);
        properties.setProperty("mail.smtp.auth", "true");

        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    /**
     * 发送邮件
     *
     * @param toAddress 收件人邮箱地址
     * @param subject   邮件主题
     * @param content   邮件内容
     */
    public void sendEmail(String toAddress, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    public String generateRandomCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Generate a random number between 1000 and 9999
        return String.valueOf(code);
    }

}

