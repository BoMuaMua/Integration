package com.example.gateway.entity;

import lombok.Data;

import java.util.List;

@Data
public class RoutesMenu {

    String name;
    String component;
    String path;
    String icon;
    Meta meta;
    List<RoutesMenu> children;

}
