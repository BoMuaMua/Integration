package com.example.gateway.entity;

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
 * @since 2024-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("PermissionGroups")
@Schema(name = "PermissionGroups", title = "权限组")
public class PermissionGroups implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "权限组id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "权限组名称")
    private String name;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "描述")
    private String description;
}
