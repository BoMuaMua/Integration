package com.example.gateway.handler;


import com.example.gateway.utils.ResponseResultUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.info("登录失败: {}", exception.getMessage());

        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        return ResponseResultUtil.response(response, 401, "登录失败: " + exception.getMessage());
    }
}
