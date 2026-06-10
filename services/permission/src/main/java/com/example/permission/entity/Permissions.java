package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("Permissions")
@Schema(name = "权限")
public class Permissions implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "权限id")
    private Long id;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "权限状态")
    private Integer status;
}
