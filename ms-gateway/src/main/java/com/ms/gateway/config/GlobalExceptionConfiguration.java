package com.ms.gateway.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.gateway.code.CommonCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-1)
@RequiredArgsConstructor
@Configuration
@Component
public class GlobalExceptionConfiguration implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.info("GlobalExceptionConfiguration:" + exchange.getRequest().getURI());
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // header set
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        var dataBufferMono = Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                JSONObject json = new JSONObject();
                json.put("code", CommonCodeConst.SERVICE_ERROR);
                json.put("msg", ex.getMessage());

                return bufferFactory.wrap(objectMapper.writeValueAsBytes(json));
            } catch (JsonProcessingException e) {
                log.warn("Error writing response", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        });

        return response.writeWith(dataBufferMono);
    }


}
