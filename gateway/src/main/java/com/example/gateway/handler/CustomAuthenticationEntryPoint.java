package com.example.gateway.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.warn("Authentication failed in reactive context: {}", ex.getMessage());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String message = ex.getMessage();
        if ("An Authentication object was not found in the SecurityContext".equals(message)) {
            message = "未通过认证";
        }
        // 这里需要根据实际的ResponseResultUtil实现来调整
        return response.writeWith(Mono.just(response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8))));
    }
}
