package com.example.permission.controller;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.Roles;
import com.example.permission.entity.dto.RolesDTO;
import com.example.permission.service.RolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "角色")
@RequestMapping("/roles")
public class RolesController {

    @Resource
    private RolesService rolesService;

    @Operation(summary = "获取角色列表")
    @GetMapping("/get")
    public ResponseResult getRolesList() {
        return new ResponseResult(200, "查询成功", rolesService.getRolesList());
    }

    @Operation(summary = "添加角色")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody RolesDTO roles) {
        return rolesService.addRoles(roles);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestParam Integer roleId) {
        return rolesService.deleteRoles(roleId);
    }

    @Operation(summary = "修改角色")
    @PutMapping("/update")
    public ResponseResult update(@RequestBody Roles roles) {
        return rolesService.updateRoles(roles);
    }

    @Operation(summary = "获取权限组")
    @GetMapping("/getPermissionGroups")
    public ResponseResult getPermissionGroups(@RequestParam Integer roleId){
        return rolesService.getPermissionGroups(roleId);
    }
}
