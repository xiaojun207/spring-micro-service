package com.microservice.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservice.auth.entity.User;
import com.microservice.auth.service.UserService;
import com.microservice.starter.annotation.Strategy;
import com.microservice.starter.model.AuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("UserController")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Strategy(strategyService = {"AuthStrategyService"})
    @GetMapping("/list")
    public Page<User> UserList(Pageable pageable) {
        // /ms-auth/user/list?page=1&size=10
        //  log.info("pageable:" + pageable.getClass().getName() + ",number:" + pageable.getPageNumber()+ ",size:" + pageable.getPageSize() + ",pageable:" + pageable);
        return userService.getList(pageable);
    }

    @Strategy(strategyService = {"AuthStrategyService", "SmsStrategyService"})
    @PostMapping("/add")
    public Long addUser(@RequestBody User req) {
        userService.add(req);
        return req.getUid();
    }

    @Strategy(strategyService = {"AuthStrategyService", "SmsStrategyService"})
    @PostMapping("/update")
    public Long updateUser(@RequestBody User req) {
        userService.update(req);
        return req.getUid();
    }

    @Strategy(strategyService = {"AuthStrategyService", "SmsStrategyService"})
    @PostMapping("/delete")
    public Long deleteUser(@RequestBody User req) {
        userService.deleteById(req.getUid());
        return req.getUid();
    }

    @Strategy(strategyService = {"AuthStrategyService"})
    @GetMapping("/info")
    public JSONObject get(AuthContext authContext) {
        log.info("User.getInfo.authContext.uid:" + authContext.getUid());

        JSONObject res = new JSONObject();
        res.put("uid", authContext.getUid());
        res.put("username", "admin");
        res.put("avatar", "http://www.abc.com/ab.png");
        res.put("roles", new String[]{"admin"});
        return res;
    }
}
