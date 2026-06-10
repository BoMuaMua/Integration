package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Elements")
public class Elements {
    private Integer elementsId;
    private String elementsName;
    private String content;
    private Integer indicatorsId;
    private Integer standardScore;
}
