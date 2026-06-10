package com.example.permission.entity.dto;

import com.example.permission.entity.Permissions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PermissionGroupsDTO {
    @Schema(description = "权限组名称")
    private String name;
    @Schema(description = "描述")
    private String description;
    private List<Permissions> permissions;
    private Integer status;
}
