package com.example.permission.entity;

import lombok.Data;

import java.util.List;

@Data
public class IndicatorsMessage {
    private Integer indicatorsId;
    private String indicatorsName;
    private List<Elements> elements;
}
