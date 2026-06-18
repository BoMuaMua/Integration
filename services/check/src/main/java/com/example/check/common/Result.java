package com.example.check.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    // 静态工厂方法 (不带数据)
    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    // 静态工厂方法 (带数据)
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    // 静态工厂方法 (带错误信息)
    public static Result error(String message) {
        return new Result(500, message, null);
    }
}