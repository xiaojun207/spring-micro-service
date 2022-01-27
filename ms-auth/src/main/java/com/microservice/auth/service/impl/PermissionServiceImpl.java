package com.microservice.auth.service.impl;

import com.microservice.auth.entity.Role;
import com.microservice.auth.entity.RoleUri;
import com.microservice.auth.entity.Uri;
import com.microservice.auth.entity.UserRole;
import com.microservice.auth.repository.IRoleRepository;
import com.microservice.auth.repository.IRoleUriRepository;
import com.microservice.auth.repository.IUriRepository;
import com.microservice.auth.repository.IUserRoleRepository;
import com.microservice.auth.service.PermissionService;
import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    IUriRepository uriRepository;
    @Autowired
    IUserRoleRepository userRoleRepository;
    @Autowired
    IRoleUriRepository roleUriRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void loadRoleUriToRedis() {
        // 考虑到数据量有限，就不做细颗粒度更新缓存了
        redisTemplate.delete("role:uri:*");
        redisTemplate.delete("user:role:*");

        roleUriRepository.findAll().stream().forEach(a -> redisTemplate.boundSetOps("role:uri:" + a.getRoleId()).add(a.getUri()));
        userRoleRepository.findAll().stream().forEach(a -> redisTemplate.boundSetOps("user:role:" + a.getUid()).add(a.getRoleId()));
    }

    @Override
    public void checkAuth(Long uid, String path) {
        Set<Long> roles = redisTemplate.boundSetOps("user:role:" + uid).members();
        for (Long roleId : roles.stream().toList()) {
            boolean isMember = redisTemplate.boundSetOps("role:uri:" + roleId).isMember(path);
            if (isMember) {
                return;
            }
        }
        throw new AppException(CommonCodeConst.NO_PERMISSION, "没有权限");
    }

    @Override
    public List<Role> getRoleList() {
        return roleRepository.findAll();
    }

    @Override
    public void addRole(Role role) {
        roleRepository.saveAndFlush(role);
    }

    @Override
    public void updateRole(Role role) {
        roleRepository.saveAndFlush(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void addRoleUri(Long roleId, String uri) {
        RoleUri roleUri = new RoleUri();
        roleUri.setRoleId(roleId);
        roleUri.setUri(uri);
        roleUriRepository.saveAndFlush(roleUri);
        this.loadRoleUriToRedis();
    }

    @Override
    public void addUserRole(Long uid, Long... roleIds) {
        List<UserRole> list = Arrays.stream(roleIds).map(roleId -> new UserRole(uid, roleId)).collect(Collectors.toList());
        userRoleRepository.saveAllAndFlush(list);
        this.loadRoleUriToRedis();
    }

    @Override
    public void deleteUserRole(Long uid, Long... roleIds) {
        List<Long> list = Arrays.stream(roleIds).collect(Collectors.toList());
        userRoleRepository.deleteAllByUidAndRoleIds(uid, list);
        this.loadRoleUriToRedis();
    }


    @Override
    public List<Uri> getUriList() {
        return uriRepository.findAll();
    }

    @Override
    public void addUri(Uri uri){
        uriRepository.saveAndFlush(uri);
    }

}
