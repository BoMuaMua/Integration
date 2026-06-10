package com.example.signin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("Activity")
public class ActivityDetail {
    @TableId(value = "activity_id")
    private Integer activityId;
    @TableField(value = "activity_name")
    private String activityName;
    private Integer typeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Long> userId;
    private List<Integer> departmentId;
    private String description;
}
