package com.microservice.auth.service.impl;

import com.microservice.auth.entity.SmsLog;
import com.microservice.auth.repository.SmsLogRepository;
import com.microservice.auth.service.SmsService;
import com.microservice.auth.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService{

    // 验证码有效期，5分钟
    public static final long SMS_EXPIRE_TIME = 60 * 5;

    @Autowired
    UserService userService;
    @Autowired
    SmsLogRepository smsLogRepository;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String sendCode(Long uid) {
        String mobile = userService.findById(uid).getMobile();
        return this.sendCode(mobile);
    }

    @Override
    public String sendCode(String mobile) {
        String cacheCode = (String) redisTemplate.boundValueOps("sms:code:" + mobile).get();
        if (StringUtils.isNotBlank(cacheCode)){
            return cacheCode;
        }

        String code = RandomStringUtils.randomNumeric(6);
        // 调用运营商api发送短信
        // aliyunApi.sendSms(mobile, code);

        var msg = new SmsLog();
        msg.setMobile(mobile);
        msg.setMsg(code);
        smsLogRepository.saveAndFlush(msg);

        // 短信验证码存入redis
        redisTemplate.boundValueOps("sms:code:" + mobile).set(code, SMS_EXPIRE_TIME, TimeUnit.SECONDS);
        return code;
    }

    @Override
    public boolean checkCode(Long uid, String smsCode) {
        String mobile = userService.findById(uid).getMobile();
        return this.checkCode(mobile, smsCode);
    }

    @Override
    public boolean checkCode(String mobile, String smsCode) {
        String code = (String) redisTemplate.boundValueOps("sms:code:" + mobile).get();
        return StringUtils.equalsIgnoreCase(code, smsCode);
    }

    @Override
    public void delete(Long uid) {
        String mobile = userService.findById(uid).getMobile();
        this.delete(mobile);
    }

    @Override
    public void delete(String mobile) {
        redisTemplate.delete("sms:code:" + mobile);
    }
}
