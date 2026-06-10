package com.example.permission.controller;

import com.example.permission.entity.PermissionGroups;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PermissionGroupsDTO;
import com.example.permission.service.PermissionGroupsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "权限组")
@RequestMapping("/permissionGroups")
public class PermissionGroupsController {

    @Resource
    private PermissionGroupsService permissionGroupsService;

    @Operation(summary = "获取权限组列表")
    @GetMapping("/get")
    public ResponseResult getPermissionGroupsList() {
        return permissionGroupsService.getPermissionGroupsList();
    }

    @Operation(summary = "添加权限组")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody PermissionGroupsDTO permissionGroups) {
        return permissionGroupsService.addPermissionGroups(permissionGroups);
    }

    @Operation(summary = "删除权限组")
    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestParam Integer permissionGroupsId) {
        return permissionGroupsService.deletePermissionGroups(permissionGroupsId);
    }

    @Operation(summary = "修改权限组")
    @PutMapping("/update")
    public ResponseResult update(@RequestBody PermissionGroups permissionGroups) {
        return permissionGroupsService.updatePermissionGroups(permissionGroups);
    }
    @Operation(summary = "将权限添加至权限组")
    @PostMapping("/add-permissions")
    public ResponseResult addPermissionsToGroup(@RequestParam Integer groupId, @RequestBody List<Integer> permissionIds) {
        return permissionGroupsService.addPermissionsToGroup(groupId, permissionIds);
    }
    @Operation(summary = "从权限组中移除权限")
    @DeleteMapping("/remove-permissions")
    public ResponseResult removePermissionsFromGroup(@RequestParam Integer groupId, @RequestBody List<Integer> permissionIds) {
        return permissionGroupsService.removePermissionsFromGroup(groupId, permissionIds);
    }
}

