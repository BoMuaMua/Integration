package com.example.signin.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security用户信息
 */
@Data
public class CustomUserDetails implements UserDetails {
    private User user;
    private String username;
    private String password;
    private List<Routes> routes;
    private List<String> permissions; // 更改为List<String>

    public CustomUserDetails(User user, List<Routes> routes) {
        this.user = user;
        this.username = user.getId().toString();
        this.routes = routes;
        this.permissions = routes.stream()
                .map(Routes::getPath) // 提取path参数
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() != null && user.getStatus() == 1;
    }
}
