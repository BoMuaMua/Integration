package com.example.permission.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserWithRoutes {
    private User user;
    private List<RoutesMenu> routes;
}
