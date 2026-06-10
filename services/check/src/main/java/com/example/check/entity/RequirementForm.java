package com.example.check.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.DateTimeException;
import java.util.Date;

@Data
@Entity
@Table(name = "requirement_form")
public class RequirementForm {
    //  需求ID
    @Id
    @Column(name = "requirement_id")
    private String requirementID;
// 主任务ID

    @Column(name = "main_task_id")
    private int mainTaskID;
    //    任务详情
    @Column(name = "requirement_name")
    private String requirementName;

    @Column(name = "requirement_detail")
    private String requirementDetail;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "tag")
    private char tag;

    @Column(name = "other_info")
    private String otherInfo;
}
