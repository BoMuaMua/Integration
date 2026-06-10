package com.example.signin.service;


import com.example.signin.entity.Departments;
import com.example.signin.entity.ResponseResult;

import java.util.List;

public interface DepartmentsService {
    List<Departments> getDepartmentsList();

    ResponseResult getDepartmentDetail(Integer departmentId);
}
