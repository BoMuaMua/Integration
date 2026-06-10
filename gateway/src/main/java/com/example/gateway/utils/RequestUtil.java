package com.example.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
@Component
public class RequestUtil {
    public static Mono<JSONObject> requestBodyToJSONObject(ServerWebExchange exchange) {
        return exchange.getRequest().getBody()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                .reduce(new StringBuilder(), (sb, str) -> sb.append(str))
                .map(sb -> {
                    String body = sb.toString();
                    if (body.isEmpty()) {
                        throw new AuthenticationServiceException("请求体不能为空");
                    }
                    return JSON.parseObject(body);
                });
    }
}

