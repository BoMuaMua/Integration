package com.example.gateway.Service;


import com.example.gateway.entity.ResponseResult;
import com.example.gateway.entity.User;

public interface UserService {
    ResponseResult register(User user);
}
