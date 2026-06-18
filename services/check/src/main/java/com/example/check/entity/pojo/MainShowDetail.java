package com.example.check.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@TableName("main_task")
@Data
public class MainShowDetail {
    @TableId(value = "main_task_id",type = IdType.AUTO)
    private int mainTaskId;
    @TableField(value = "tag")
    private int tag;
    @TableField(value = "task_name")
    private String taskName;
    @TableField(value = "date_limit")
    private Date dateLimit;
    @TableField(value = "teacher")
    private String teacher;
    @TableField(value = "department")
    private String department;
    @TableField(value = "phone_number")
    private String phoneNumber;
    @TableField(value = "office_address")
    private String officeAddress;
}
