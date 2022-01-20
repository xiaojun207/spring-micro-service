package com.example.mstest.controller;

import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/user")
    public String user(AuthContext context) {
        return "context:" + context;
    }

    @GetMapping("/test")
    public String test() {
        return "This is starter test";
    }

    @GetMapping("/void")
    public void testVoid() {
        System.out.println("testVoid");
    }

    @GetMapping("/exception")
    public String exception() throws Exception {
        throw new Exception("TestException");
    }

    @GetMapping("/appException")
    public String AppException() {
        throw new AppException("100101", "TestException");
    }

}
