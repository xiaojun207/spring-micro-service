package com.microservice.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class InnerApi {

    @Autowired
    RedisTemplate redisTemplate;

    public String getInnerApiToken() {
        // 后续：需要绑定url和参数，并签名
        // 随机token，并放入redis，2秒钟有效
        String apiToken = UUID.randomUUID().toString();
        redisTemplate.boundValueOps("innerApi:" + apiToken).set( "1", 2, TimeUnit.SECONDS);
        return apiToken;
    }

    // 验证token 是否存在
    public boolean CheckInnerApiToken(String innerApiToken)  {
        if (StringUtils.isBlank(innerApiToken)) {
            return false;
        }
        log.info("innerApiToken:" + innerApiToken);
        // 验证redis中，是否包含token
        String res = (String) redisTemplate.boundValueOps("innerApi:" + innerApiToken).get();
        return "1".equals(res);
    }

}
