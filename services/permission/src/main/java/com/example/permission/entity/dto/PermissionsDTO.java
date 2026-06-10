package com.example.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PermissionsDTO {
    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限标识")
    private String perms;
}
