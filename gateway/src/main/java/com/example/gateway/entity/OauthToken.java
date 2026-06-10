package com.example.gateway.entity;

import lombok.Data;

@Data
public class OauthToken {
    private String access_token;
    private String token_type;
    private Integer expires_in;
}
