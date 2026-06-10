package com.example.permission.service;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public interface AuthorizationDecisionService {
    AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object);
}
