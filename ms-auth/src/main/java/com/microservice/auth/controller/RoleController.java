package com.microservice.auth.controller;

import com.microservice.auth.entity.Role;
import com.microservice.auth.service.PermissionService;
import com.microservice.auth.strategy.AuthStrategyService;
import com.microservice.auth.strategy.InnerApiStrategyService;
import com.microservice.auth.strategy.SmsStrategyService;
import com.microservice.starter.annotation.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("RoleController")
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private PermissionService permissionService;

    @Strategy(strategyService = {"AuthStrategyService"})
    @GetMapping("/list")
    public List<Role> roleList() {
        return permissionService.getRoleList();
    }

    @Strategy(strategyService = {"AuthStrategyService"})
    @PostMapping("/add")
    public Long addRole(@RequestBody Role role) {
        permissionService.addRole(role);
        return role.getId();
    }

    @Strategy(strategyClass = {AuthStrategyService.class, SmsStrategyService.class})
    @PostMapping("/update")
    public Long updateRole(@RequestBody Role role) {
        permissionService.updateRole(role);
        return role.getId();
    }

    @Strategy(strategyService = {"AuthStrategyService", "SmsStrategyService"})
    @PostMapping("/delete")
    public Long deleteRole(@RequestBody Role role) {
        permissionService.deleteRole(role.getId());
        return role.getId();
    }

}
