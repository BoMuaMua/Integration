package com.example.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.gateway.Service.Impl.CustomUserDetailsServiceImpl;
import com.example.gateway.entity.CustomUserDetails;
import com.example.gateway.handler.CustomAuthenticationSuccessHandler;
import com.example.gateway.utils.JwtUtils;
import com.example.gateway.utils.RedisUtil;
import com.example.gateway.utils.ResponseResultUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

/**
 * Gateway Token认证过滤器（WebFlux版本）
 */
@Component
@Slf4j
public class GatewayAuthenticationFilter implements WebFilter {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    @Resource
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 放行Option请求
        String method = String.valueOf(request.getMethod());
        if ("OPTIONS".equals(method)) {
            //结束此过滤，继续下一个过滤器
            return chain.filter(exchange);
        }
        String requestToken = request.getHeaders().getFirst(JwtUtils.getCurrentConfig().getHeader());
        if (StringUtils.isNotBlank(requestToken)) {
            if (JwtUtils.isTokenExpired(requestToken)) {
                return chain.filter(exchange);
            }
        }

        CustomUserDetails userDetails;
        try {
            String subject = JwtUtils.getSubject(requestToken);
            HashMap<String, String> token = JSON.parseObject(subject, HashMap.class);
            String id = token.get("id");
            String expirationTime = token.get("Expiration time");
            HashMap<String, String> map = (HashMap<String, String>) redisUtil.get(id);
            // 判断Redis是否过期
            LocalDateTime now = LocalDateTime.now();
            if (map == null) {
                if (!Objects.isNull(redisUtil.get("isReLogin:" + id))) {
                    throw new Exception("Token已失效");
                }
                if (now.isBefore(LocalDateTime.parse(expirationTime).plusDays(1))) {
                    userDetails = (CustomUserDetails) customUserDetailsServiceImpl.loadUserByUsername(id);
                    ResponseResultUtil.response(response, 205, "Token已刷新", customAuthenticationSuccessHandler.generateToken(userDetails));
                } else {
                    throw new Exception("Token已失效");
                }
                return chain.filter(exchange);
            } else {
                if (!requestToken.equals("Bearer " + map.get("token"))) {
                    throw new Exception("Token无效");
                }
                userDetails = JSON.parseObject(map.get("userDetails"), CustomUserDetails.class);
            }
        } catch (JWTDecodeException jwtDecodeException) {
            if (request.getPath().equals("/login")) {
                return chain.filter(exchange);
            }
            ResponseResultUtil.response(response, 401, "Token无效");
            return chain.filter(exchange);
        } catch (Exception e) {
            if (request.getPath().equals("/login")) {
                return chain.filter(exchange);
            }
            ResponseResultUtil.response(response, 401, e.getMessage());
            return chain.filter(exchange);
        }

        Boolean isUpdateInformation = (Boolean) redisUtil.get("isUpdateInformation:" + userDetails.getUser().getId());
        if (isUpdateInformation != null) {
            userDetails = (CustomUserDetails) customUserDetailsServiceImpl.loadUserByUsername(userDetails.getUser().getId().toString());
            HashMap<String, String> map = new HashMap<>();
            map.put("token", requestToken.substring(7));
            map.put("userDetails", JSON.toJSONString(userDetails));
            redisUtil.set(userDetails.getUser().getId().toString(), map);
            redisUtil.del("isUpdateInformation:" + userDetails.getUser().getId());
            log.info("用户信息已更新({})", userDetails.getUsername());
        }

        // 保存用户信息到当前会话
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 将authentication填充到安全上下文并继续过滤器链
        return ReactiveSecurityContextHolder.getContext()
                .defaultIfEmpty(new SecurityContextImpl())
                .flatMap(securityContext -> {
                    Authentication existingAuth = securityContext.getAuthentication();

                    if (existingAuth != null && existingAuth.isAuthenticated()) {
                        // 直接继续过滤器链，不修改上下文
                        return chain.filter(exchange);
                    } else {
                        // 设置新的认证信息并继续过滤器链
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    }
                });
    }
}