package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.service.EmailService;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
         SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
         simpleMailMessage.setFrom(Constant.EMAIL_FROM);
         simpleMailMessage.setTo(to);
         simpleMailMessage.setSubject(subject);
         simpleMailMessage.setText(text);
         mailSender.send(simpleMailMessage);
    }

    @Override
    public boolean saveEmailTorRedis(String emailAddress, String verificationCode) {
        //Redisson.create()返回的client默认是我们本机的Redis
         RedissonClient redissonClient = Redisson.create();
         //一般的储存是运用了key:value的形式，而这中是表示emailAddress是key值
         //redissonClient.getBucket(emailAddress)，表示获得key所代表的Value值
        RBucket<String> bucket = redissonClient.getBucket(emailAddress);
         boolean exists = bucket.isExists();
        if (!exists) {
            bucket.set(verificationCode,60, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

}
