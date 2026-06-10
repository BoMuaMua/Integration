package com.example.permission.entity;

import lombok.Data;

import java.util.List;

@Data
public class AddPeople {
    private Integer activityId;
    private List<Long> userId;
}
