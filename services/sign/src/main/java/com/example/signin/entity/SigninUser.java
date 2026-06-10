package com.example.signin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("SigninUser")
@Data
public class SigninUser {
    @TableId("id")
    private Long id;
    @TableField("user_name")
    private String userName;
    @TableField("department_id")
    private int departmentId;
    @TableField("role_id")
    private int roleId;
    @TableField("signin_time")
    private String signinTime;
    @TableField("activity_name")
    private String activityName;
    @TableField("status")
    private int status;
}
