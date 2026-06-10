package com.example.signin.entity;

import lombok.Data;

import java.util.List;
@Data
public class Routes {
    private String name;
    private String path;
    private String component;
    private String icon;
    private Meta meta;
    private List<Routes> children;
}
