package com.example.permission.service.impl;

import com.example.permission.mapper.PermissionsMapper;
import com.example.permission.service.AuthorizationDecisionService;
import com.example.permission.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class AuthorizationDecisionServiceImpl implements AuthorizationDecisionService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private PermissionsMapper permissionsMapper;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        // 获取当前的访问路径
        String requestURI = object.getRequest().getRequestURI();
        // 判断是否已登录
        if (!authentication.get().isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        // 获取访问该路径所需权限
        List<String> perms = (List<String>) redisUtil.get("perms:" + requestURI);
        // 如果缓存中没有则查询数据库
        if (perms == null) {
            perms = permissionsMapper.selectPermsByRoute(requestURI);
            redisUtil.setWithExpire("perms:" + requestURI, perms, 5, TimeUnit.MINUTES);
        }

        // 如果接口没有配置权限则直接放行
        if (perms.isEmpty()) {
            return new AuthorizationDecision(true);
        }
        // 获取当前登录用户权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
        // 判断当前用户是否有足够的权限访问
        if (perms.stream().anyMatch(authority -> authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals(authority)))) {
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }
}
