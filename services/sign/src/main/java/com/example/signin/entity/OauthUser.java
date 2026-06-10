package com.example.signin.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class OauthUser implements OAuth2User {
    private User user;
    private List<Routes> routes;
    private Collection<? extends GrantedAuthority> authorities;  // 用户权限信息
    private Map<String, Object> attributes;  // OAuth2 用户属性

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return Long.toString(this.user.getId());
    }

}
