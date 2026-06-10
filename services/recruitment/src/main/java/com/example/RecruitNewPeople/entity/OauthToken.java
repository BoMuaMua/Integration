package com.example.RecruitNewPeople.entity;

import lombok.Data;

@Data
public class OauthToken {
    private String code;
    private String msg;
    private String data;
}
