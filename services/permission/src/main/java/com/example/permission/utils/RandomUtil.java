package com.example.permission.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomUtil {
    // 定义生成随机字符串的方法
    public  String generateRandomString(int length) {
        // 定义随机字符串的字符集
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
