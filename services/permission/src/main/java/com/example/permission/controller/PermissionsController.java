package com.example.permission.controller;

import com.example.permission.entity.Permissions;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PermissionsDTO;
import com.example.permission.service.PermissionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "权限")
@RequestMapping("/permissions")
public class PermissionsController {

    @Resource
    private PermissionsService permissionsService;

    @Operation(summary = "获取权限列表")
    @GetMapping ("/get")
    public ResponseResult getMenus(){
        return permissionsService.getPermissionList();
    }

    @Operation(summary = "添加权限")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody PermissionsDTO permissions){
        return permissionsService.addPermission(permissions);
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestParam String permissionId){
        return permissionsService.deletePermission(permissionId);
    }

    @Operation(summary = "修改权限")
    @PutMapping("/update")
    public ResponseResult update(@RequestBody Permissions permissions){
        return permissionsService.updatePermission(permissions);
    }

    @Operation(summary = "根据权限组id获取权限列表")
    @GetMapping("/getByGroupId")
    public ResponseResult getByGroupId(@RequestParam String groupId){
        return permissionsService.getByGroupId(groupId);
    }
}
