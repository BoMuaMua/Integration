package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("Activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Integer activityId;
    private String activityName;
    private String description;
    private int year;
}
