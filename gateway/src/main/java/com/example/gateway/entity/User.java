package com.example.gateway.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 1m1ng
 * @since 2024-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("User")
@Schema(name = "用户实体类")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户学号")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "用户名字")
    @TableField("username")
    private String username;

    @Schema(description = "部门ID")
    @TableField("department_id")
    private Integer departmentId;

    @Schema(description = "年级")
    @TableField("grade")
    private Integer grade;

    @Schema(description = "角色ID")
    @TableField("role_id")
    private Integer roleId;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private String createTime;

    @Schema(description = "状态")
    @TableField("status")
    private Integer status;

}
