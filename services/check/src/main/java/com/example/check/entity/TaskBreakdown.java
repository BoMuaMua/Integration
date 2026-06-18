package com.example.check.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


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
    private String taskDetail;

//    要求时间
    @Column(name = "deadline")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime deadline;

    //    任务负责人‘
    @Column(name = "officer")
    private String officer;

//    负责管理员
    @Column(name = "administrator")
    private String administrator;
//    管理员联系方式
    @Column(name = "admin_phone")
    private String adminPhone;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createTime;
}