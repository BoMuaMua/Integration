package com.example.permission.service;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.AdminUpdateUserDTO;

public interface AdminService {
    ResponseResult informationUser();

    ResponseResult updateUser(AdminUpdateUserDTO user);

    ResponseResult deleteUser(String userId);
}
