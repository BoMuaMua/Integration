package com.example.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.example.gateway.entity.CustomOAuth2User;
import com.example.gateway.entity.OauthToken;
import com.example.gateway.entity.OauthUser;
import com.example.gateway.handler.CustomAuthenticationSuccessHandler;
import com.example.gateway.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class CustomOauth2AuthenticationFilter implements WebFilter {

    private final WebClient webClient;
    private final CustomAuthenticationSuccessHandler successHandler;

    // 白名单路径 - 明确指定只处理/login
    private final Set<String> whiteList = Set.of("/error", "/actuator/health");

    public CustomOauth2AuthenticationFilter(WebClient.Builder webClientBuilder,
                                            CustomAuthenticationSuccessHandler successHandler) {
        this.webClient = webClientBuilder.build();
        this.successHandler = successHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        // 跳过非POST的/login请求和其他白名单路径
        if (!path.equals("/login") || whiteList.contains(path)) {
            return chain.filter(exchange);
        }

        // 只处理POST请求的/login路径
        if (method != HttpMethod.POST) {
            return chain.filter(exchange);
        }

        log.info("Processing OAuth2 login request");

        return processOAuth2Login(exchange, chain);
    }

    private Mono<Void> processOAuth2Login(ServerWebExchange exchange, WebFilterChain chain) {
        return RequestUtil.requestBodyToJSONObject(exchange)
                .flatMap(jsonObject -> {
                    String code = jsonObject.getString("code");
                    if (code == null || code.trim().isEmpty()) {
                        return Mono.error(new AuthenticationServiceException("Authorization code is required"));
                    }

                    log.info("OAuth2 code received: {}", code);
                    return getToken(code);
                })
                .flatMap(token -> {
                    if (token == null || token.getAccess_token() == null) {
                        return Mono.error(new AuthenticationServiceException("Failed to get OAuth2 token"));
                    }
                    return getUserInfo(token.getAccess_token());
                })
                .flatMap(userInfo -> {
                    if (userInfo == null) {
                        return Mono.error(new AuthenticationServiceException("Invalid OAuth2 token"));
                    }

                    log.info("OAuth2 user info received: {}", userInfo.getName());

                    // 创建认证对象
                    Map<String, Object> attributes = Map.of(
                            "number", userInfo.getNumber(),
                            "name", userInfo.getName(),
                            "college", userInfo.getCollege()
                    );
                    CustomOAuth2User oAuth2User = new CustomOAuth2User(userInfo, attributes);
                    OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(
                            oAuth2User, oAuth2User.getAuthorities(), "custom-client");

                    // 直接调用成功处理器，不再继续过滤器链
                    return successHandler.onAuthenticationSuccess(
                            new org.springframework.security.web.server.WebFilterExchange(exchange, chain),
                            authenticationToken
                    );
                })
                .onErrorResume(e -> {
                    log.error("OAuth2 authentication failed: {}", e.getMessage());
                    return Mono.error(new AuthenticationServiceException("OAuth2 authentication failed: " + e.getMessage()));
                });
    }

    private Mono<OauthToken> getToken(String code) {
        return webClient.post()
                .uri("https://auth.gcu.edu.cn/api/application/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "grant_type", "authorization_code",
                        "code", code,
                        "client_id", "123456",
                        "client_secret", "y47mmU8nxoP984uU",
                        "redirect_uri", "http%3A%2F%2F43.139.169.119%3A3637%2F%23%2Fhome"
                ))
                .retrieve()
                .bodyToMono(OauthToken.class)
                .doOnSuccess(token -> log.info("OAuth2 token retrieved successfully"))
                .doOnError(e -> log.error("Failed to get OAuth2 token: {}", e.getMessage()));
    }

    private Mono<OauthUser> getUserInfo(String token) {
        return webClient.post()
                .uri("https://auth.gcu.edu.cn/api/application/oauth/data")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("access_token", token))
                .retrieve()
                .bodyToMono(OauthUser.class)
                .doOnSuccess(user -> log.info("User info retrieved successfully"))
                .doOnError(e -> log.error("Failed to get user info: {}", e.getMessage()));
    }
}