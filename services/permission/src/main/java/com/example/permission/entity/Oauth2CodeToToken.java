package com.example.permission.entity;

import lombok.Data;

@Data
public class Oauth2CodeToToken {
    private String code;
    private String clientId;
    private String clientSecret;
}
