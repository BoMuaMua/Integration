package com.example.signin.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelDTO {
    public ExcelDTO(String statisticalId, String statisticalName, Double signInRate) {
        StatisticalId = statisticalId;
        StatisticalName = statisticalName;
        this.signInRate = signInRate;
    }
    @ExcelProperty("统计对象id")
    private String StatisticalId;
    @ExcelProperty("统计对象名称")
    private String StatisticalName;
    @ExcelProperty("签到率")
    private Double signInRate;
}
