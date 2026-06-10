package com.example.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminAddUserDTO {

    @Schema(description = "用户学号")
    private Long id;

    @Schema(description = "用户名字")
    private String username;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "部门ID")
    private Integer departmentId;

    @Schema(description = "年级")
    private Integer grade;

    @Schema(description = "角色ID")
    private Integer roleId;

    @Schema(description = "头像")
    private String email;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "状态")
    private Integer status;

}
