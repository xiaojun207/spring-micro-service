package com.ms.gateway.filter;

import com.alibaba.fastjson.JSONObject;

import com.ms.gateway.code.CommonCodeConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.ms.gateway.config.AuthExclusion;
import com.ms.gateway.service.PermissionService;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthExclusion exclusion;
    @Autowired
    private PermissionService permissionService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        log.info("request path:{}", path);
        //1、判断是否是过滤的路径， 是的话就放行
        if (exclusion.isExclusionUrl(path) ){
            return chain.filter(exchange);
        }
        //2、判断请求的URL是否有权限
        boolean hasAuth = permissionService.checkAuth(request);
        if (!hasAuth){
            return getVoidMono(response, CommonCodeConst.NO_PERMISSION, "无访问权限");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    private Mono<Void> getVoidMono(ServerHttpResponse response, String code, String msg) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);

        byte[] bits = json.toJSONString().getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        return response.writeWith(Mono.just(buffer));
    }

}
