package com.ms.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.ms.gateway.config.AuthExclusion;
import com.ms.gateway.feign.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthExclusion exclusion;
    @Autowired
    private AuthClient authClient;
    ExecutorService executorService = Executors.newFixedThreadPool(1);


    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
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
        boolean hasAuth = checkAuth(request);
        if (!hasAuth){
            return getVoidMono(response, 403, "无访问权限");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private boolean checkAuth(ServerHttpRequest request){
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
       return "100200".equals(resp.getString("code"));
    }

    private Mono<Void> getVoidMono(ServerHttpResponse response, int code, String msg) {
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
