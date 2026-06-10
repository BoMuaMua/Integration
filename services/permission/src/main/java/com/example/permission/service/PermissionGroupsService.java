package com.example.permission.service;

import com.example.permission.entity.PermissionGroups;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PermissionGroupsDTO;

import java.util.List;

public interface PermissionGroupsService {
    ResponseResult getPermissionGroupsList();

    ResponseResult addPermissionGroups(PermissionGroupsDTO permissionGroups);

    ResponseResult deletePermissionGroups(Integer permissionGroupsId);

    ResponseResult updatePermissionGroups(PermissionGroups permissionGroups);

    ResponseResult addPermissionsToGroup(Integer groupId, List<Integer> permissionIds);

    ResponseResult removePermissionsFromGroup(Integer groupId, List<Integer> permissionIds);
}
