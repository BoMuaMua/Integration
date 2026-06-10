package com.example.signin.utils;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EasyExcelUtil {
    //最简单的写
    public static <T> List<T> easyWrite(List<T> list, HttpServletResponse response, Class<T> customClasses) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 获取 Class 对象对应的类名
        String className = customClasses.getSimpleName();
        // 对类名进行 URL 编码
        String encodedClassName = URLEncoder.encode(className, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedClassName + ".xlsx");

        // 使用 EasyExcel 写入 Excel 文件到响应流，并指定 Sheet 名称为类名
        EasyExcel.write(response.getOutputStream(), customClasses).sheet(className).doWrite(list);

        return list;
    }
}
