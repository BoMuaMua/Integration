package com.example.signin.entity;

import lombok.Data;

@Data
public class SigninMessage {
    private String yesOrNoAdministrator;
    private Activity activity;
    private SponsorLocation sponsorLocation;
}
