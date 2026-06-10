package com.example.permission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.permission.mapper")
public class PermissionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermissionsApplication.class, args);
    }

}
