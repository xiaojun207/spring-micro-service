package com.microservice.auth.service;

public interface SmsService {

    String sendCode(Long uid);
    String sendCode(String mobile);

    boolean checkCode(Long uid, String smsCode);
    boolean checkCode(String mobile, String smsCode);

    void delete(Long uid);
    void delete(String mobile);
}
