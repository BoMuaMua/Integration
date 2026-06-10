package com.example.gateway.filter;

import com.example.gateway.entity.CustomUserDetails;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserIDHeaderGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String path = exchange.getRequest().getPath().value();

                // 跳过登录路径
                if ("/login".equals(path)) {
                    return chain.filter(exchange);
                }

                // 使用响应式安全上下文
                return ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        .filter(authentication -> authentication != null && authentication.isAuthenticated())
                        .flatMap(authentication -> {
                            // 在过滤器链执行前添加header到请求中
                            if (authentication.getPrincipal() instanceof CustomUserDetails) {
                                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                                String userId = customUserDetails.getUsername();

                                // 创建新的请求，添加用户ID头信息
                                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                        .header(config.getName(), userId)
                                        .build();

                                // 使用修改后的请求创建新的exchange
                                ServerWebExchange modifiedExchange = exchange.mutate()
                                        .request(modifiedRequest)
                                        .build();

                                return chain.filter(modifiedExchange);
                            }
                            return chain.filter(exchange);
                        })
                        .switchIfEmpty(chain.filter(exchange)); // 如果没有认证信息，继续执行
            }
        };
    }
}