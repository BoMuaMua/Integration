package com.example.check.entity.pojo;
import lombok.Data;

import java.util.Date;

@Data
public class MainShowInfo {
    private int mainTaskID;
    private int tag;
    private String taskName;
    private Date dateLimit;
    private String teacher;

}