package com.example.signin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2024-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Departments")
@Schema(name = "Departments", title = "部门")
public class Departments implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "部门id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门状态")
    @TableField("status")
    private Integer status;
}
