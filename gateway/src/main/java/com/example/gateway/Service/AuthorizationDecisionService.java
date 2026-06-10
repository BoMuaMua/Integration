package com.example.gateway.Service;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public interface AuthorizationDecisionService {
    Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext authorizationContext);
}
