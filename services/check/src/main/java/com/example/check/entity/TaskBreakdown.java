package com.example.check.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "task_breakdown")
public class TaskBreakdown {
//    主任务ID
    @Id
    @Column(name = "main_task_id")
    private int mainTaskId;

//    拆分任务ID
    @Column(name = "breakdown_id")
    private int breakdownId;

//    任务名称
    @Column(name = "task_name")
    private String taskName;

    //    任务状态
    @Column(name = "tag")
    private int tag;

//    任务详情
    @Column(name = "task_detail")
    private String taskDetiail;

//    要求时间
    @Column(name = "deadline")
    private Date deadline;

    //    任务负责人
    @Column(name = "officer")
    private String officer;

//    负责管理员
    @Column(name = "administrator")
    private String administrator;
//    管理员联系方式
    @Column(name = "admin_phone")
    private long adminPhone;
}
