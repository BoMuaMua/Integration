package com.example.RecruitNewPeople.entity.pojo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "响应请求参数")
public class ResponseResult {

    @Schema(description = "响应码", example = "200", required = true)
    private Integer code;
    @Schema(description = "响应信息",example = "响应成功",required = true)
    private String msg;
    @Schema(description = "响应数据",example = "data",required = true)
    private Object data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
