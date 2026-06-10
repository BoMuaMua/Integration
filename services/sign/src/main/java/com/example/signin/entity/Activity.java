package com.example.signin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("Activity")
public class Activity{
    @TableId(value = "activity_id")
    private Integer activityId;
    private String activityName;
    private String typeName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Long> userId;
    private List<Integer> departmentId;
    private String description;
}