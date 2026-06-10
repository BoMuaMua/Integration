package com.example.permission.controller;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.CustomUserDetails;
import com.example.permission.utils.RedisUtil;
import com.example.permission.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SecurityController {

    @Resource
    private RedisUtil redisUtil;

    @Operation(summary = "注销")
    @GetMapping("/logout")
    public ResponseResult logout() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityUtils.setUserReLogin(redisUtil, userDetails.getUsername());
        redisUtil.del(userDetails.getUsername());
        log.info("注销成功(" + userDetails.getUsername() + ")");
        return new ResponseResult(200, "注销成功!", null);
    }

}
