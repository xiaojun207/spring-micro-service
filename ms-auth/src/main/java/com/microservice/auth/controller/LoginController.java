package com.microservice.auth.controller;

import com.microservice.auth.dto.UserDto;
import com.microservice.auth.service.LoginService;
import com.microservice.auth.service.PermissionService;
import com.microservice.starter.annotation.Strategy;
import com.microservice.starter.model.AuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
@RestController("LoginController")
@RequestMapping("/")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    PermissionService permissionService;

    @PostMapping("/login")
    public String login(@RequestBody UserDto req) {
        return loginService.login(req);
    }

    @Strategy(strategyService = {"SmsStrategyService"})
    @PostMapping("/smsLogin")
    public String smsLogin(@RequestBody UserDto req) {
        return loginService.login(req);
    }

    @GetMapping("/isvalid")
    public Long validToken(String authorization) {
        var authContext = AuthContext.build(authorization);
        return loginService.getUidByToken(authContext.getToken());
    }

    @GetMapping("/checkAuth")
    public void checkAuth(String authorization, String path) {
        var authContext = AuthContext.build(authorization);
        Long uid = loginService.getUidByToken(authContext.getToken());
        permissionService.checkAuth(uid, path);
    }

    @PostMapping("/logout")
    public void logout(AuthContext authContext) {
        log.info("login.logout:" + authContext);
        loginService.logout(authContext);
    }
}
