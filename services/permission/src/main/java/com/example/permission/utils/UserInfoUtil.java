package com.example.permission.utils;

import com.example.permission.entity.User;
import com.example.permission.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoUtil {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;

    public User getUserInfo(String userId) {
        if (redisUtil.get(userId) != null){
            return (User) redisUtil.get(userId);
        }else {
            User user = userMapper.selectById(userId);
            redisUtil.set(userId,user);
            return user;
        }
    }

    public List<User> selectByDepartmentId(Integer departmentId){
        if (redisUtil.get(departmentId.toString()) != null){
            return (List<User>) redisUtil.get(departmentId.toString());
        }else {
            List<User> users = userMapper.selectByDepartmentId(departmentId);
            redisUtil.set(departmentId.toString(),users);
            return users;
        }
    }
}

