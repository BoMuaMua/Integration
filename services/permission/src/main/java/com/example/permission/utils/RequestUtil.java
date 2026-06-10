package com.example.permission.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.io.BufferedReader;

public class RequestUtil {
    public static JSONObject requestBodyToJSONObject(HttpServletRequest request) {
        StringBuilder requestBody = new StringBuilder();
        try{
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }catch (Exception e){
            throw new AuthenticationServiceException("非法传参");
        }
        return JSON.parseObject(requestBody.toString());
    }
}
