package com.example.signin.entity;

import lombok.Data;

@Data
public class OauthToken {
    private String code;
    private String msg;
    private String data;
}
