package com.example.signin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("activityType")
public class ActivityType {
    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;
    @TableField("type_name")
    private String typeName;
}
