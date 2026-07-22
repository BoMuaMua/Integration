package common;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InfoUser {

    @Schema(description = "用户学号")
    private Long id;

    @Schema(description = "用户名字")
    private String username;

    @Schema(description = "部门id")
    private Integer departmentId;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "年级")
    private Integer grade;

    @Schema(description = "角色id")
    private Integer roleId;

    @Schema(description = "角色")
    private String role;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "状态")
    private Integer status;
}
