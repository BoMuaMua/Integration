package com.example.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminUpdateUserDTO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名字")
    private String username;

    @Schema(description = "部门ID")
    private Integer departmentId;

    @Schema(description = "年级")
    private Integer grade;

    @Schema(description = "角色ID")
    private Integer roleId;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "状态")
    private Integer status;
}
