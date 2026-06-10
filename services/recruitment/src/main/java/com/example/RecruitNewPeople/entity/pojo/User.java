package com.example.RecruitNewPeople.entity.pojo;

import lombok.Data;

import java.util.List;

@Data
public class User<U> {
    private String userId;
    private List<String> imageURLs;

}