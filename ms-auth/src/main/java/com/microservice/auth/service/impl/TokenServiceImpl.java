package com.microservice.auth.service.impl;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.microservice.auth.service.TokenService;
import com.microservice.auth.utils.TokenHelper;
import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String createToken(Long uid) {
        String token = TokenHelper.getToken(uid, 60 * 60 * 24);
        redisTemplate.boundValueOps("token:" + token).set(uid, 60 * 60 * 24, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public Long getUidByToken(String token) {
        try {
            // check Token in redis
            if(!redisTemplate.hasKey("token:" + token)){
                throw new AppException(CommonCodeConst.NO_LOGIN, "token已过期");
            }
            return TokenHelper.getTokenUid(token);
        } catch (TokenExpiredException e) {
            throw new AppException(CommonCodeConst.NO_LOGIN, "token已过期");
        } catch (Exception e) {
            throw new AppException(CommonCodeConst.NO_LOGIN, "token无效");
        }
    }

    @Override
    public void deleteToken(String token) {
        redisTemplate.delete("token:" + token);
    }
}
