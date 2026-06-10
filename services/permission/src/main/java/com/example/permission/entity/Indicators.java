package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("Indicators")
public class Indicators {
    @TableId(value = "indicators_id", type = IdType.AUTO)
    private Integer indicatorsId;
    private String indicatorsName;
}
