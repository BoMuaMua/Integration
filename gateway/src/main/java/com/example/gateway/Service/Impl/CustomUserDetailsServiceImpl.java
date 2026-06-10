package com.example.gateway.Service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gateway.entity.CustomUserDetails;
import com.example.gateway.entity.User;
import com.example.gateway.mapper.PermissionsMapper;
import com.example.gateway.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Objects;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionsMapper permissionsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, username);
        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 获取用户权限
        List<String> permission = permissionsMapper.selectPermsByUserId(Long.valueOf(username));
        // 返回SecurityUserDetails
        return new CustomUserDetails(user, permission);
    }
}
