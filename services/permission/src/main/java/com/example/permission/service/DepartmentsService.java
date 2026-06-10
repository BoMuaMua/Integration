package com.example.permission.service;

import com.example.permission.entity.Departments;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.DepartmentsDTO;

import java.util.List;

public interface DepartmentsService {
    List<Departments> getDepartmentsList();

    ResponseResult addDepartment(DepartmentsDTO departments);

    ResponseResult deleteDepartment(Integer departmentId);

    ResponseResult updateDepartment(Departments departments);

    ResponseResult getDepartmentDetail(Integer departmentId);
}
