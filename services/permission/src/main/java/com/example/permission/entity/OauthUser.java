package com.example.permission.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class OauthUser implements OAuth2User {
    private String number;    // 用户编号
    private String name;      // 用户名
    private String college;   // 用户所属学院
    private Collection<? extends GrantedAuthority> authorities;  // 用户权限信息
    private Map<String, Object> attributes;  // OAuth2 用户属性

    // 构造方法
    public OauthUser(String number, String name, String college, String email, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.number = number;
        this.name = name;
        this.college = college;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    // 实现OAuth2User接口的方法

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
        return this.name;  // OAuth2中通常作为唯一标识符，可以使用`id`或`name`
    }
}
