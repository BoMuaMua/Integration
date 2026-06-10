package com.example.RecruitNewPeople.controller;


import com.alibaba.fastjson.JSON;
import com.example.RecruitNewPeople.Service.DepartmentService;
import com.example.RecruitNewPeople.mapper.DepartmentMapper;
import com.example.RecruitNewPeople.utils.JwtUtils;
import com.example.RecruitNewPeople.utils.RedisUtil;
import com.example.RecruitNewPeople.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DepartmentController {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    DepartmentService department;
    @Autowired
    RedisUtil redisUtil;
@GetMapping("/user/pass")
    ResultUtil pass(@RequestParam String departmentId){
    if (!( departmentMapper.departmentExist(departmentId)))
        return  new ResultUtil(400,"无此部门",null);
    return  department.choosePassPerson(departmentId);
}
    @GetMapping("/user/noPass")
    ResultUtil Nopass(@RequestParam String departmentId){
        if (!( departmentMapper.departmentExist(departmentId)))
            return  new ResultUtil(400,"无此部门",null);
        return  department.chooseNoPassPerson(departmentId);
    }
    @GetMapping("/user/willPass")
    ResultUtil willPass (@RequestParam String departmentId){
        if (!( departmentMapper.departmentExist(departmentId)))
            return  new ResultUtil(400,"无此部门",null);
        return  department.chooseWillPassPerson(departmentId);
    }
    @PostMapping("user/nextDepartment")
    ResultUtil nextDepartment(@RequestBody Map<String ,Object > map){
    String id= (String) map.get("id");
    if(!(departmentMapper.userExist(id))) return  new ResultUtil(400,"没有此id信息",null);
    if(department.nextDepartment(id))
        return ResultUtil.success();
    return ResultUtil.error();
    }
    @PostMapping("/user/status")
    ResultUtil changeStatus(@RequestBody Map<String ,Object > map){
        String id= (String) map.get("id");
        if(!(departmentMapper.userExist(id))) return  new ResultUtil(400,"没有此id信息",null);
        String status= (String) map.get("status");
        System.out.println(status);
       //判断是否为数字
        if (! (status.matches("-?\\d+(\\.\\d+)?"))) return  new ResultUtil(400,"status参数错误",null);
        Integer statu =Integer.parseInt(status);
        if ((department.changeStatus(id,statu))) return  ResultUtil.success();
        return  new ResultUtil(403,"status取值错误",null);
    }

    @GetMapping("/getDepartment")
    ResultUtil getDepartment(HttpServletRequest request){
        String requestToken = request.getHeader(JwtUtils.getCurrentConfig().getHeader());
        String subject = JwtUtils.getSubject(requestToken);
        HashMap<String, String> tokenData = JSON.parseObject(subject, HashMap.class);
        Integer departmentId = Integer.parseInt(tokenData.get("departmentId"));
        return ResultUtil.success(departmentId);
    }
}
