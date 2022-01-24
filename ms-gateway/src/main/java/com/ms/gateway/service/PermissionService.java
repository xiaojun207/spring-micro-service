package com.ms.gateway.service;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface PermissionService {
    boolean checkAuth(ServerHttpRequest request);
}
