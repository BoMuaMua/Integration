package com.example.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoutesDTO {

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "路由组件")
    private String component;

    @Schema(description = "路由父id")
    private Long parent;

    @Schema(description = "路由图标")
    private String icon;

    @Schema(description = "路由权限")
    private Long perm;
}
