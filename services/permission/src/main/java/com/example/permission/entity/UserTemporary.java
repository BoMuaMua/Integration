package com.example.permission.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTemporary {
    private Long id;
    private LocalDateTime expirationTime;
}
