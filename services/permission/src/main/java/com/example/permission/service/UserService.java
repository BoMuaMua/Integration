package com.example.permission.service;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PersonUpdateUserDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseResult information(HttpServletRequest request);

    ResponseResult update(HttpServletRequest request,PersonUpdateUserDTO user);

    ResponseResult allInformation(Integer departmentId);
}
