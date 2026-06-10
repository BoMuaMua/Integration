package com.example.permission.controller;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.AdminUpdateUserDTO;
import com.example.permission.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Operation(summary = "查询用户信息")
    @GetMapping("/informationUser")
    public ResponseResult informationUser() {
        return adminService.informationUser();
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/updateUser")
    public ResponseResult updateUser(@RequestBody AdminUpdateUserDTO user) {
        return adminService.updateUser(user);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/deleteUser")
    public ResponseResult deleteUser(@RequestParam String userId) {
        return adminService.deleteUser(userId);
    }
}
