package com.example.gateway.handler;

import com.example.gateway.utils.ResponseResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Autowired
    private ResponseResultUtil responseResultUtil;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.warn("Access denied: {}", denied.getMessage());
        ServerHttpResponse serverResponse = exchange.getResponse();
        ResponseResultUtil.response(serverResponse, 401, "Access denied");
        return serverResponse.setComplete();
    }
}
