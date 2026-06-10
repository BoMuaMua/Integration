package com.example.signin.entity;

import lombok.Data;

import java.util.List;

@Data
public class OauthUserInfo {
    private String code;
    private String msg;
    private InnerData data;

    @Data
    public static class InnerData {
        private User user;
        private List<Routes> routes;
    }
}

