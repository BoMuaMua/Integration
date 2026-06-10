package com.example.signin.utils;

import com.example.signin.entity.OauthUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    @Getter
    private final OauthUser userDetails;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(OauthUser userDetails, Map<String, Object> attributes) {
        this.userDetails = userDetails;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;  // 返回 OAuth2 用户的属性，如 email、name 等
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();  // 返回用户的权限信息
    }

    @Override
    public String getName() {
        return Long.toString(userDetails.getUser().getId());  // 返回用户的唯一标识（学号）
    }

}
