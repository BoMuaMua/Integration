package com.example.permission.entity;

import lombok.Data;

import java.util.List;

@Data
public class Participants {
    private Activity activity;
    private List<User> participants;
}
