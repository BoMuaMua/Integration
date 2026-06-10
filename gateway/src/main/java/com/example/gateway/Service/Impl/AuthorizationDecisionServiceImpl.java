package com.example.gateway.Service.Impl;


import com.example.gateway.Service.AuthorizationDecisionService;
import com.example.gateway.mapper.PermissionsMapper;
import com.example.gateway.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizationDecisionServiceImpl implements AuthorizationDecisionService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private PermissionsMapper permissionsMapper;

@Override
public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext authorizationContext) {
    ServerHttpRequest request = authorizationContext.getExchange().getRequest();
    // 获取当前的访问路径
    String requestURI = request.getPath().value();
    
    return authenticationMono.flatMap(auth -> {
        // 判断是否已登录
        if (!auth.isAuthenticated()) {
            return Mono.just(new AuthorizationDecision(false));
        }
        
        // 获取访问该路径所需权限
        List<String> perms = (List<String>) redisUtil.get("perms:" + requestURI);
        // 如果缓存中没有则查询数据库
        if (perms == null) {
            perms = permissionsMapper.selectPermsByRoute(requestURI);
            if (perms != null) {
                redisUtil.setWithExpire("perms:" + requestURI, perms, 5, TimeUnit.MINUTES);
            }
        }

        // 如果接口没有配置权限则直接放行
        if (perms == null || perms.isEmpty()) {
            return Mono.just(new AuthorizationDecision(true));
        }
        
        // 获取当前登录用户权限信息
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        // 判断当前用户是否有足够的权限访问
        boolean hasPermission = perms.stream().anyMatch(authority -> authorities.stream()
                .anyMatch(authObj -> authObj.getAuthority().equals(authority)));
        return Mono.just(new AuthorizationDecision(hasPermission));
    });
}
}
