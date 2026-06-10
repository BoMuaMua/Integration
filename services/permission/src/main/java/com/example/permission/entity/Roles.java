package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("Roles")
@Schema(name = "Roles", title = "角色")
public class Roles implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色状态")
    private Integer status;
}
