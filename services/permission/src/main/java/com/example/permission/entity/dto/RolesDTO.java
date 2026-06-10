package com.example.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RolesDTO {
    @Schema(description = "角色id", example = "1", required = true)
    private String name;
}
