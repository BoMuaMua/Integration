package com.example.permission.service;

import com.example.permission.entity.Permissions;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PermissionsDTO;

import java.util.List;

public interface PermissionsService {
    ResponseResult getPermissionList();

    ResponseResult addPermission(PermissionsDTO permissions);

    ResponseResult deletePermission(String permissionId);

    ResponseResult updatePermission(Permissions permissions);

    ResponseResult getByGroupId(String groupId);

    /**
     * 根据用户ID获取权限列表
     * @param userId 用户ID
     * @return 权限字符串列表
     */
    List<String> getPermissionsByUserId(Long userId);
}
