package com.microservice.auth.service;

public interface TokenService {

    String createToken(Long uid);
    Long getUidByToken(String token);
    void deleteToken(String token);

}
