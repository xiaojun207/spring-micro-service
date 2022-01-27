package com.microservice.auth.service;

import com.microservice.auth.entity.Role;
import com.microservice.auth.entity.Uri;

import java.util.List;

public interface PermissionService {
    void checkAuth(Long uid, String uri);

    List<Role> getRoleList();
    void addRole(Role role);
    void updateRole(Role role);
    void deleteRole(Long id);

    void addRoleUri(Long roleId, String uri);

    void addUserRole(Long uid, Long ...roleId);

    void deleteUserRole(Long uid, Long ...roleId);

    List<Uri> getUriList();
    void addUri(Uri uri);
}
