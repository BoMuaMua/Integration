package com.example.signin.controller;

import com.example.signin.entity.ResponseResult;
import com.example.signin.service.DepartmentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "部门")
@RequestMapping("/departments")
public class DepartmentsController {

    @Resource
    private DepartmentsService departmentService;

    @GetMapping("/get")
    @Operation(summary = "获取部门列表")
    public ResponseResult list(){
       return new ResponseResult(200, "查询成功", departmentService.getDepartmentsList());
    }

    @Operation(summary = "获取部门详情")
    @GetMapping("/getDetail")
    public ResponseResult getDetail(@RequestParam Integer departmentId){
        return departmentService.getDepartmentDetail(departmentId);
    }
}
