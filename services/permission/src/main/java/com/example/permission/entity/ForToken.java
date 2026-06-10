package com.example.permission.entity;

import lombok.Data;

@Data
public class ForToken {
    private String encryptedData;
    private String clientId;
}
