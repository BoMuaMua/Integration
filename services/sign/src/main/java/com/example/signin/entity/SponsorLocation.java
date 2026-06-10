package com.example.signin.entity;

import lombok.Data;

@Data
public class SponsorLocation {
    //经度
    private Double latitude;
    //纬度
    private Double longitude;
    //限定的距离
    private Double distance;
}
