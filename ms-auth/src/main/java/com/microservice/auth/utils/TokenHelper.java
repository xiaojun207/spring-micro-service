package com.microservice.auth.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaojun
 */
public class TokenHelper {
    //签名随意写，但复杂性越高，安全性越高
    public static final String SING = "f9140099cabedb04858fec7db5a2b5c5";


    public static String getToken(Long uid, int expiresSecond) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("uid", uid + "");
        return getToken(payload, expiresSecond);
    }
    /**
     * 生成token
     */
    public static String getToken(Map<String, String> payload, int expiresSecond) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, expiresSecond);

        //创建JWT builder
        JWTCreator.Builder builder = JWT.create();

        //payload
        payload.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        //指定令牌的过期时间
        String token = builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));//签名
        return token;
    }

    /**
     * 验证token合法性
     */
    public static DecodedJWT verifier(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * 获取token信息
     */
    public static DecodedJWT getTokenInfo(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }

    public static Long getTokenUid(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        String uid = verify.getClaim("uid").asString();
        return Long.parseLong(uid);
    }

    public static void main2(String[] args) {
        String token = getToken(123L, 5000);
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMjMiLCJleHAiOjE2NDI3Nzk1MjN9.VNyK48VaClD1hiD_r0RenaSn_wHQMMRrMHrBuaMiXpg";
        System.out.println("token:" + token);
        DecodedJWT verify = getTokenInfo(token);
        System.out.println("verify.header:" + verify.getHeader());
        System.out.println("verify.getAudience:" + verify.getAudience());
        System.out.println("verify.uid:" + getTokenUid(token));

        System.out.println("verify.payload:" + verify.getPayload());
        System.out.println("verify.signature:" + verify.getSignature());
    }

}
