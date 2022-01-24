package com.microservice.auth.controller;

import com.microservice.starter.annotation.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("PermissionController")
@RequestMapping("/permission")
public class PermissionController {

    @Strategy(strategyService = {"AuthStrategyService", "SmsStrategyService"})
    @GetMapping("/roles")
    public String get() {
        return "this is a get test";
    }

    @PostMapping("/post")
    public String post(@RequestBody Object request) {
        return "this is a set test:" + request;
    }

}
