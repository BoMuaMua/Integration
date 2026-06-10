package com.example.gateway.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gateway.mapper.PermissionsMapper;
import com.example.gateway.mapper.UserMapper;
import com.example.gateway.entity.CustomUserDetails;
import com.example.gateway.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    public UserDetailServiceImpl(UserMapper userMapper, PermissionsMapper permissionsMapper) {
        this.userMapper = userMapper;
        this.permissionsMapper = permissionsMapper;
    }

    private PermissionsMapper permissionsMapper;
    private UserMapper userMapper;
//    暂缺从数据库调用数据的引入

    /**
     * 重写用于从数据库获取用户信息，当redis中有时从redis中调用以节省时间，若没有则从数据库中调用并存入redis数据库
     * @param id 将spring security中的UserDetailService接口中的loadUserByUsername重写，故只能使用username，可将所需内容从username转换为其余内容使用
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<String> permission = permissionsMapper.selectPermsByUserId(Long.valueOf(id));
        return new CustomUserDetails(user, permission);
    }
}
