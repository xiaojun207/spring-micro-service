package com.ms.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ms.gateway.code.CommonCodeConst;
import com.ms.gateway.feign.AuthClient;
import com.ms.gateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private AuthClient authClient;
    ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public boolean checkAuth(ServerHttpRequest request){
        String authorization = request.getHeaders().getFirst("authorization");
        String path = request.getURI().getPath();
        log.info("checkAuth:" + path + ", authorization:" + authorization);
        Future<JSONObject> future = executorService.submit(() -> authClient.checkAuth(authorization, path));
        JSONObject resp = null;
        try {
            resp = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        log.info("checkAuth.resp:" + resp);
        return CommonCodeConst.SUCCESS.equals(resp.getString("code"));
    }



}
