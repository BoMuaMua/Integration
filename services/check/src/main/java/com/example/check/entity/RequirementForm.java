package com.example.check.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;


@Data
@Entity
@Table(name = "requirement_form")
public class RequirementForm {

    @Id
    @TableId(value = "requirement_id", type = IdType.AUTO)
    private int requirementID;

    @Column(name = "main_task_id")
    private int mainTaskID;

    @Column(name = "requirement_detail")
    private String requirementDetail;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "tag")
    private int tag;

    @Column(name = "other_info")
    private String otherInfo;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "office_address")
    private String officeAddress;


}
