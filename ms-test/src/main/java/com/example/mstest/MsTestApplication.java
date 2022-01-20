package com.example.mstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.mstest", "com.microservice.starter"})
public class MsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTestApplication.class, args);
    }

}
