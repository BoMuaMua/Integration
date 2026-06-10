package com.example.permission.utils;

import com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SecurityUtils {

    public static void setUserReLogin(RedisUtil redisUtil, String userId){
        HashMap<String, String> map = (HashMap<String, String>) redisUtil.get(userId);
        String token = "Bearer " + map.get("token");
        map = JSON.parseObject(JwtUtils.getSubject(token), HashMap.class);
        LocalDateTime expirationTime = LocalDateTime.parse(map.get("Expiration time"));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(expirationTime.plusDays(1))) {
            long seconds = expirationTime.toEpochSecond(ZoneOffset.of("+8")) - now.toEpochSecond(ZoneOffset.of("+8"));
            redisUtil.setWithExpire("isReLogin:" + userId, true, seconds, TimeUnit.SECONDS);
            redisUtil.del(userId);
        }
    }

    public static void setUserUpdateInformation(RedisUtil redisUtil, String userId){
        HashMap<String, String> map = (HashMap<String, String>) redisUtil.get(userId);
        if(map == null){
            return;
        }
        String token = "Bearer " + map.get("token");
        map = JSON.parseObject(JwtUtils.getSubject(token), HashMap.class);
        LocalDateTime expirationTime = LocalDateTime.parse(map.get("Expiration time"));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(expirationTime)) {
            long seconds = expirationTime.toEpochSecond(ZoneOffset.of("+8")) - now.toEpochSecond(ZoneOffset.of("+8"));
            redisUtil.setWithExpire("isUpdateInformation:" + userId, true, seconds, TimeUnit.SECONDS);
        }
    }
}
