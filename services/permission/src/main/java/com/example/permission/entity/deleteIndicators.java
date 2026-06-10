package com.example.permission.entity;

import lombok.Data;

import java.util.List;

@Data
public class deleteIndicators {
    private Integer indicatorsId;
    private List<Integer> elementsId;
}
