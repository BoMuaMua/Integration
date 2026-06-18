package com.example.permission.controller;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PersonUpdateUserDTO;
import com.example.permission.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Tag(name = "用户")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "获取个人信息")
    @GetMapping("/information")
    public ResponseResult information(HttpServletRequest  request) {
        return userService.information(request);
    }

    @Operation(summary = "更新个人信息")
    @PutMapping("/update")
    public ResponseResult update(HttpServletRequest request, @RequestBody PersonUpdateUserDTO user) {
        return userService.update(request,user);
    }

    @Operation(summary = "获取所有个人信息")
    @GetMapping("/allInformation")
    public ResponseResult allInformation(@RequestParam Integer departmentId) {
        System.out.println(departmentId);
        return userService.allInformation(departmentId);
    }
}
