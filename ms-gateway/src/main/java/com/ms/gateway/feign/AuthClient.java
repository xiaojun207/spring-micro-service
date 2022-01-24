package com.ms.gateway.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-auth", path = "/ms-auth")
public interface AuthClient {

    @RequestMapping(value = "/checkAuth", method = RequestMethod.GET)
    JSONObject checkAuth(@RequestParam String authorization, @RequestParam String path);
}
