package com.microservice.auth.controller;

import com.microservice.auth.entity.Role;
import com.microservice.auth.entity.Uri;
import com.microservice.auth.service.PermissionService;
import com.microservice.starter.annotation.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("PermissionController")
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Strategy(strategyService = {"AuthStrategyService"})
    @PostMapping("/uri/add")
    public Long addRole(@RequestBody Uri req) {
        permissionService.addUri(req);
        return req.getId();
    }

    @Strategy(strategyService = {"AuthStrategyService"})
    @GetMapping("/uri/list")
    public List<Uri> uriList() {
        return permissionService.getUriList();
    }

}
