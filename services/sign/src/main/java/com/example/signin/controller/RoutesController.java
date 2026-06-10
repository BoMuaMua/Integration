package com.example.signin.controller;

import com.example.signin.entity.CustomUserDetails;
import com.example.signin.entity.ResponseResult;
import com.example.signin.utils.ResponseResultUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutesController {

    @GetMapping("/routes/getMenus")
    public ResponseResult getMenus() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseResult(200, "success", customUserDetails.getRoutes());
    }
}
