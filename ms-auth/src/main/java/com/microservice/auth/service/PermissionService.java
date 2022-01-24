package com.microservice.auth.service;

public interface PermissionService {
    void checkAuth(Long uid, String uri);

    void addRole(String name);

    void addRoleUri(Long roleId, String uri);

    void addUserRole(Long uid, Long ...roleId);

    void deleteUserRole(Long uid, Long ...roleId);

}
