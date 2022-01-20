package com.microservice.auth.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController("LoginController")
@RequestMapping("/")
public class LoginController {

    @PostMapping("/login")
    public String post(@RequestBody JSONObject req, HttpServletRequest request, HttpServletResponse response) {
        log.info("login.post:" + req);
        String username = req.getString("username");
        String password = req.getString("password");
        if("admin".equals(username) && "123456".equals(password)){
            String token = "TestToken";
            response.addCookie(new Cookie("authorization", token));
            response.addHeader("authorization", token);
            return token;
        }
        return "this is a set test:" + req;
    }

    @GetMapping("/checkAuth")
    public void checkAuth(String token, String path) throws Exception {
        log.info("token:" + token + ", path:" + path);
        if ("TestToken".equals(token)) {
            return;
        }
        throw new Exception("Token失效");
    }

}
