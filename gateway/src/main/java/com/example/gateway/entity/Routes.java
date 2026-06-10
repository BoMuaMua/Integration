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
@TableName("Routes")
@Schema(name = "Routes", title = "路由")
public class Routes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "路由id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    @Schema(description = "路由状态")
    private Integer status;

    @Schema(description = "所属子系统")
    private Integer clientId;
}
