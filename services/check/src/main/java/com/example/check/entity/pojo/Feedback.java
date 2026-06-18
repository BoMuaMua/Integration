package com.example.check.entity.pojo;

import lombok.Data;


@Data
public class Feedback {
    private long id;
    private int score;
    private int tag;
    private String context;
}
