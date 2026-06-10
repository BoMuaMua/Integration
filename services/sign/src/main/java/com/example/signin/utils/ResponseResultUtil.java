package com.example.signin.utils;

import com.alibaba.fastjson.JSON;
import com.example.signin.entity.ResponseResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 提供用于构建和发送响应结果的工具方法类。
 */
@Slf4j
public class ResponseResultUtil {

    /**
     * 构建并发送包含状态码、消息和数据的响应结果。
     *
     * @param response 用于发送响应的HttpServletResponse对象。
     * @param code 响应的状态码。
     * @param msg 响应的消息。
     * @param data 响应的数据。
     */
    public static void response(HttpServletResponse response, Integer code, String msg, Object data) {
        ResponseResult result = new ResponseResult(code, msg, data);
        String json = JSON.toJSONString(result);
        renderString(response, json);
    }

    /**
     * 构建并发送包含状态码和消息的响应结果，不包含数据。
     *
     * @param response 用于发送响应的HttpServletResponse对象。
     * @param code 响应的状态码。
     * @param msg 响应的消息。
     */
    public static void response(HttpServletResponse response, Integer code, String msg){
        response(response,code,msg,null);
    }

    /**
     * 构建并发送包含状态码和数据的响应结果，不包含消息。
     *
     * @param response 用于发送响应的HttpServletResponse对象。
     * @param code 响应的状态码。
     * @param data 响应的数据。
     */
    public static void response(HttpServletResponse response,Integer code, Object data){
        response(response,code,null,data);
    }

    /**
     * 将字符串渲染到客户端。
     * 主要用于将JSON格式的字符串作为响应体发送给客户端。
     *
     * @param response 渲染对象，用于发送响应。
     * @param string 待渲染的字符串，通常是JSON格式的数据。
     */
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }
}
