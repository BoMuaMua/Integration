package com.example.RecruitNewPeople.Service;


import com.example.RecruitNewPeople.utils.ResultUtil;

public interface DepartmentService {
    boolean nextDepartment(String id);


    ResultUtil choosePassPerson(String departmentId);

    ResultUtil chooseNoPassPerson(String departmentId);

    ResultUtil chooseWillPassPerson(String departmentId);

    boolean changeStatus(String id, Integer status);
}
