package com.example.RecruitNewPeople.entity.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class throughMembers {
    @ExcelProperty("姓名")
    private String username;
    @ExcelProperty("学号")
    private String id;
    @ExcelProperty("介绍")
    private String introduction;
    @ExcelProperty("专业")
    private String major;
    @ExcelProperty("学院")
    private String college;
    @ExcelProperty("电话")
    private String phone;
    @ExcelProperty("性别")
    private String gender;
    @ExcelProperty("录用部门")
    private String departmentName;

}
