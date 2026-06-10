package com.example.gateway.utils;

import com.alibaba.fastjson.JSON;

import com.example.gateway.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供用于构建和发送响应结果的工具方法类。
 */
@Slf4j
public class ResponseResultUtil {

    /**
     * 构建并发送包含状态码、消息和数据的响应结果。
     *
     * @param response 用于发送响应的ServerHttpResponse对象。
     * @param code 响应的状态码。
     * @param msg 响应的消息。
     * @param data 响应的数据。
     */
    public static void response(ServerHttpResponse response, Integer code, String msg, Object data) {
        ResponseResult result = new ResponseResult(code, msg, data);
        String json = JSON.toJSONString(result);
        renderString(response, json);
    }

    /**
     * 构建并发送包含状态码和消息的响应结果，不包含数据。
     *
     * @param response 用于发送响应的ServerHttpResponse对象。
     * @param code 响应的状态码。
     * @param msg 响应的消息。
     */
    public static void response(ServerHttpResponse response, Integer code, String msg){
        response(response,code,msg,null);
    }

    /**
     * 构建并发送包含状态码和数据的响应结果，不包含消息。
     *
     * @param response 用于发送响应的ServerHttpResponse对象。
     * @param code 响应的状态码。
     * @param data 响应的数据。
     */
    public static void response(ServerHttpResponse response,Integer code, Object data){
        response(response,code,null,data);
    }

    /**
     * 将字符串渲染到客户端。
     * 主要用于将JSON格式的字符串作为响应体发送给客户端。
     *
     * @param response 渲染对象，用于发送响应。
     * @param string 待渲染的字符串，通常是JSON格式的数据。
     */
    public static void renderString(ServerHttpResponse response, String string) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.writeWith(Mono.just(response.bufferFactory().wrap(string.getBytes(StandardCharsets.UTF_8))))
                .subscribe();
    }

    public static Mono<Void> response(ServerHttpResponse response, int code, String message) {
        return response(response, code, message, null);
    }

    public static Mono<Void> response(ServerHttpResponse response, int code, String message, Object data) {
        response.setStatusCode(HttpStatus.valueOf(code));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", data);

        String jsonResult = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory()
                .wrap(jsonResult.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
