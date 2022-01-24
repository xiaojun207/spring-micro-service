package com.microservice.auth.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController("UserController")
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public JSONObject get() {
        JSONObject res = new JSONObject();
        res.put("uid", 1);
        res.put("username", "admin");
        res.put("avatar", "http://www.abc.com/ab.png");
        res.put("roles", new String[]{"admin"});
        return res;
    }

    @PostMapping("/post")
    public String post(@RequestBody Object request) {
        return "this is a set test:" + request;
    }

}
