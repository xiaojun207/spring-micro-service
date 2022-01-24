package com.microservice.auth.controller;

import com.microservice.auth.dto.UserDto;
import com.microservice.auth.service.LoginService;
import com.microservice.auth.service.PermissionService;
import com.microservice.starter.model.AuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController("LoginController")
@RequestMapping("/")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    PermissionService permissionService;

    @PostMapping("/login")
    public String login(@RequestBody UserDto req, HttpServletResponse response, AuthContext authContext) {
        log.info("login.login:" + req);
        String token = loginService.login(req, authContext);
        response.addCookie(new Cookie("authorization", token));
        response.addHeader("authorization", token);
        return token;
    }

    @GetMapping("/checkAuth")
    public void checkAuth(String token, String path) throws Exception {
        Long uid = loginService.getUidByToken(token);
        permissionService.checkAuth(uid, path);
        throw new Exception("Token失效");
    }

}
