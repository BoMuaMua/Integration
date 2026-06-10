package com.example.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PersonUpdateUserDTO {
    @Schema(description = "用户名字")
    private String username;

    @Schema(description = "年级")
    private Integer grade;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "电话")
    private String phone;
}
