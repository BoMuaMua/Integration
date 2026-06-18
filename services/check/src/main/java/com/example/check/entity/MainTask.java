package com.example.check.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

//主任务表
@Data
@Entity
@Table(name = "main_task")
public class MainTask {
    @Id
    //    主任务ID
    @Column(name = "main_task_id")
    private int mainTaskID;

    //    任务状态
    @Column(name = "tag")
    private int tag;

    //    任务名称
    @Column(name = "task_name")
    private String taskName;

    //    要求时间
    @Column(name = "datelimit")
    private Date dateLimit;

    //    创建时间
    @Column(name = "create_time")
    private Date createTime;

    //    委托老师姓名
    @Column(name = "teacher")
    private String teacher;

    //    所属部门 | 学院
    @Column(name = "department")
    private String department;

    //    联系方式
    @Column(name = "phone_number")
    private String phoneNumber;

    //    办公地址
    @Column(name = "office_address")
    private String officeAddress;

    //    满意度
    @Column(name = "satisfaction")
    private int satisfaction;

    //    反馈
    @Column(name = "feedback")
    private String feedback;
}
