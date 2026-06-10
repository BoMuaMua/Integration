package com.example.check.entity.pojo;


import lombok.Data;

import java.util.Date;

@Data
public class SonPostInfo {
    private int mainTaskID;
    private int requirementID;
    private String requirementName;
    private String requirementDetail;
    private Date createTime;
    private String otherInfo;
}
