package com.example.permission.controller;

import com.example.permission.entity.Departments;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.DepartmentsDTO;
import com.example.permission.service.DepartmentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "部门")
@RequestMapping("/departments")
public class DepartmentsController {

    @Resource
    private DepartmentsService departmentService;

    @GetMapping ("/get")
    @Operation(summary = "获取部门列表")
    public ResponseResult list(){
       return new ResponseResult(200, "查询成功", departmentService.getDepartmentsList());
    }

    @Operation(summary = "添加部门")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody DepartmentsDTO departments){
        return departmentService.addDepartment(departments);
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestParam Integer departmentId){
        return departmentService.deleteDepartment(departmentId);
    }

    @Operation(summary = "修改部门")
    @PutMapping("/update")
    public ResponseResult update(@RequestBody Departments departments){
        return departmentService.updateDepartment(departments);
    }

    @Operation(summary = "获取部门详情")
    @GetMapping("/getDetail")
    public ResponseResult getDetail(@RequestParam Integer departmentId){
        return departmentService.getDepartmentDetail(departmentId);
    }
}
