package com.example.permission.entity;

import lombok.Data;

@Data
public class UserScore {
    private Long userId;
    private String username;
    private int year;
    private Integer totalScore;
    private String status;
}
