package com.example.permission.service;

import com.example.permission.entity.Permissions;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PermissionsDTO;

public interface PermissionsService {
    ResponseResult getPermissionList();

    ResponseResult addPermission(PermissionsDTO permissions);

    ResponseResult deletePermission(String permissionId);

    ResponseResult updatePermission(Permissions permissions);

    ResponseResult getByGroupId(String groupId);
}
