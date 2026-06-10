package com.example.permission.service;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.Roles;
import com.example.permission.entity.dto.RolesDTO;

import java.util.List;

public interface RolesService {
    List<Roles> getRolesList();

    ResponseResult addRoles(RolesDTO roles);

    ResponseResult deleteRoles(Integer roleId);

    ResponseResult updateRoles(Roles roles);

    ResponseResult getPermissionGroups(Integer roleId);
}
