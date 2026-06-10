package com.example.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @Schema(description = "用户学号", example = "20221012345", required = true)
    private Long id;

    @Schema(description = "用户密码", example = "12345678", required = true)
    private String password;

    @Schema(description = "用户姓名", example = "张三", required = true)
    private String username;

    @Schema(description = "部门id", example = "1", required = true)
    private Integer departmentId;

    @Schema(description = "用户邮箱", example = "abc123@abc.com", required = true)
    private String email;

    @Schema(description = "用户手机号", example = "12345678901", required = true)
    private Long phone;

    @Schema(description = "验证码", example = "123456", required = true)
    private String code;
}
