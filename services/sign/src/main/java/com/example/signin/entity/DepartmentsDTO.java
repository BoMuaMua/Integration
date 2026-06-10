package com.example.signin.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DepartmentsDTO {

    @Schema(description = "部门名称")
    private String name;
}
