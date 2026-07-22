package com.example.permission.utils;

import com.example.permission.entity.User;
import com.example.permission.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoUtil {

    private static final String USER_INFO_PREFIX = "user:info:";
    private static final String DEPT_USERS_PREFIX = "dept:users:";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;

    public User getUserInfo(String userId) {
        String cacheKey = USER_INFO_PREFIX + userId;
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof User) {
            return (User) cached;
        }
        User user = userMapper.selectById(userId);
        if (user != null) {
            redisUtil.set(cacheKey, user);
        }
        return user;
    }

    public List<User> selectByDepartmentId(Integer departmentId){
        String cacheKey = DEPT_USERS_PREFIX + departmentId;
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof List) {
            @SuppressWarnings("unchecked")
            List<User> result = (List<User>) cached;
            return result;
        }
        List<User> users = userMapper.selectByDepartmentId(departmentId);
        redisUtil.set(cacheKey, users);
        return users;
    }

    /**
     * 清除指定用户的信息缓存
     */
    public void clearUserCache(String userId) {
        redisUtil.del(USER_INFO_PREFIX + userId);
    }

    /**
     * 清除指定部门的成员列表缓存
     */
    public void clearDeptUsersCache(Integer departmentId) {
        redisUtil.del(DEPT_USERS_PREFIX + departmentId);
    }
}

