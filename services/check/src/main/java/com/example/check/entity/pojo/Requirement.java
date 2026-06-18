package com.example.check.entity.pojo;

import lombok.Data;

@Data
public class Requirement {
    private  int mainTaskId;
    private String requirementDetail;
    private String otherInfo;
    private int tag;

}
