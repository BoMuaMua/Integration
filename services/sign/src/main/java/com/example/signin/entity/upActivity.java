package com.example.signin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class upActivity {
    private Integer activityId;
    private String activityName;
    @TableField("type_id")
    private String typeName;
    private String startTime;
    private String endTime;
    private List<String> userId;
    private List<String> departmentId;
    private String description;
    private String endtime;
}
