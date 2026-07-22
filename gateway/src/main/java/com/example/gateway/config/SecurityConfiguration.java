package com.example.gateway.config;

import com.example.gateway.Service.AuthorizationDecisionService;
import com.example.gateway.documented.AnonymousAccess;
import com.example.gateway.filter.CustomOauth2AuthenticationFilter;
import com.example.gateway.filter.GatewayAuthenticationFilter;
import com.example.gateway.handler.CustomAuthenticationEntryPoint;
import com.example.gateway.handler.CustomAccessDeniedHandler;
import com.example.gateway.handler.CustomAuthenticationSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.WebFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private final CustomOauth2AuthenticationFilter customOauth2AuthenticationFilter;
    private final ApplicationContext applicationContext;
    private final GatewayAuthenticationFilter gatewayAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final AuthorizationDecisionService authorizationDecisionService;
    public SecurityConfiguration(@Lazy CustomOauth2AuthenticationFilter customOauth2AuthenticationFilter,
                                 GatewayAuthenticationFilter gatewayAuthenticationFilter,
                                 ApplicationContext applicationContext, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, AuthorizationDecisionService authorizationDecisionService) {
        this.customOauth2AuthenticationFilter = customOauth2AuthenticationFilter;
        this.gatewayAuthenticationFilter = gatewayAuthenticationFilter;
        this.applicationContext = applicationContext;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.authorizationDecisionService = authorizationDecisionService;
    }



    /**
     * 加密类
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * WebFlux安全过滤链配置 - 简化版本
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        Set<String> anonymousUrls = listAnonymous(applicationContext);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // 确保禁用表单登录
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                 .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(exchanges -> {
                    exchanges.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    anonymousUrls.forEach(url -> exchanges.pathMatchers(url).permitAll());
                    exchanges.anyExchange().access(authorizationDecisionService::check);
                })
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
//                .addFilterBefore(customOauth2AuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//                .addFilterBefore(gatewayAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    // 添加WebClient配置
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * 查找可以匿名访问的接口
     */
    /**
     * 查找可以匿名访问的接口（WebFlux版本）
     */
    public Set<String> listAnonymous(ApplicationContext applicationContext) {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();

        // 添加默认的匿名访问路径
        anonymousUrls.add("/error");
        anonymousUrls.add("/login");
        anonymousUrls.add("/**");
        anonymousUrls.add("/check/api/teacher/**"); // 支持 /check/api/teacher 及其所有子路径

        // 查找带有@AnonymousAccess注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (anonymousAccess != null) {
                // WebFlux版本的路径获取方式
                infoEntry.getKey().getPatternsCondition().getPatterns()
                        .forEach(pattern -> anonymousUrls.add(pattern.getPatternString()));
            }
        }
        return anonymousUrls;
    }
}