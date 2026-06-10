package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("User_Fraction")
public class UserFraction {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Long userId;
    private Integer activityId;
    private Integer totalScore;
    private String others;
}
