package com.example.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.example.gateway.Service.Impl.CustomUserDetailsServiceImpl;
import com.example.gateway.entity.CustomOAuth2User;
import com.example.gateway.entity.CustomUserDetails;
import com.example.gateway.entity.User;
import com.example.gateway.utils.JwtUtils;
import com.example.gateway.utils.RedisUtil;
import com.example.gateway.utils.ResponseResultUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    public String generateToken(CustomUserDetails userDetails) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", userDetails.getUsername());
        LocalDateTime now = LocalDateTime.now();
        map.put("Expiration time", now.plusDays(7).toString());
        String token = JwtUtils.generateToken(JSON.toJSONString(map));

        HashMap<String, Object> redisMap = new HashMap<>();
        redisMap.put("token", token);
        redisMap.put("userDetails", JSON.toJSONString(userDetails));
        redisUtil.setWithExpire(userDetails.getUsername(), redisMap, 7, TimeUnit.DAYS);

        return token;
    }

    public String generateTokenTemporary(CustomUserDetails userDetails) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", userDetails.getUsername());
        LocalDateTime now = LocalDateTime.now();
        map.put("Expiration time", now.plusDays(7).toString());
        String token = JwtUtils.generateToken(JSON.toJSONString(map));

        HashMap<String, Object> redisMap = new HashMap<>();
        redisMap.put("token", token);
        redisMap.put("userDetails", JSON.toJSONString(userDetails));
        redisUtil.setWithExpire(userDetails.getUsername(), redisMap, 7, TimeUnit.DAYS);

        return token;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        try {
            log.info("Authentication success handler called");

            if (!(authentication.getPrincipal() instanceof CustomOAuth2User)) {
                log.error("Invalid authentication type: {}", authentication.getPrincipal().getClass());
                return ResponseResultUtil.response(webFilterExchange.getExchange().getResponse(),
                        500, "Invalid authentication type", null);
            }

            CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
            String userNumber = oauth2User.getUserDetails().getNumber();

            log.info("Processing authentication success for user: {}", userNumber);

            // 检查Redis中是否已有token
            Object redisMap = redisUtil.get(userNumber);
            if (redisMap instanceof HashMap) {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> map = (HashMap<String, Object>) redisMap;
                String existingToken = (String) map.get("token");

                if (existingToken != null) {
                    log.info("Returning existing token for user: {}", userNumber);
                    return ResponseResultUtil.response(
                            webFilterExchange.getExchange().getResponse(),
                            200, "登录成功", existingToken
                    );
                }
            }

            // 如果没有现有token，创建新的
            return createNewToken(webFilterExchange, oauth2User, userNumber);

        } catch (Exception e) {
            log.error("Unexpected error in authentication success handler: {}", e.getMessage(), e);
            return ResponseResultUtil.response(
                    webFilterExchange.getExchange().getResponse(),
                    500, "内部错误，请稍后再试", null
            );
        }
    }

    private Mono<Void> createNewToken(WebFilterExchange webFilterExchange, CustomOAuth2User oauth2User, String userNumber) {
        return Mono.fromCallable(() -> customUserDetailsServiceImpl.loadUserByUsername(userNumber))
                .flatMap(customUserDetails -> {
                    // 用户存在，生成正式token
                    String token = generateToken((CustomUserDetails) customUserDetails);
                    log.info("Generated new token for existing user: {}", userNumber);
                    return ResponseResultUtil.response(
                            webFilterExchange.getExchange().getResponse(),
                            200, "登录成功", token
                    );
                })
                .onErrorResume(UsernameNotFoundException.class, e -> {
                    // 用户不存在，创建临时用户和token
                    User user = new User();
                    user.setId(Long.valueOf(userNumber));
                    user.setUsername(oauth2User.getUserDetails().getName());

                    CustomUserDetails temporaryUser = new CustomUserDetails(user, new ArrayList<>());
                    temporaryUser.setUser(user);
                    temporaryUser.setPermissions(new ArrayList<>());

                    String token = generateTokenTemporary(temporaryUser);
                    log.info("Generated temporary token for new user: {}", userNumber);
                    return ResponseResultUtil.response(
                            webFilterExchange.getExchange().getResponse(),
                            401, "登陆成功，生成临时token，但是用户信息不完整，请跳转至补充信息页面", token
                    );
                })
                .onErrorResume(e -> {
                    log.error("Error creating new token: {}", e.getMessage(), e);
                    return ResponseResultUtil.response(
                            webFilterExchange.getExchange().getResponse(),
                            500, "内部错误，用户处理失败", null
                    );
                });
    }


}