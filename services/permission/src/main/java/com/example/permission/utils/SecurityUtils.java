package com.example.permission.utils;

import com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SecurityUtils {

    public static void setUserReLogin(RedisUtil redisUtil, String userId){
        Object cached = redisUtil.get(userId);
        if (!(cached instanceof Map)) {
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) cached;
        String token = (String) map.get("token");
        if (token == null) {
            return;
        }
        token = "Bearer " + token;
        HashMap<String, String> jwtMap = JSON.parseObject(JwtUtils.getSubject(token), HashMap.class);
        LocalDateTime expirationTime = LocalDateTime.parse(jwtMap.get("Expiration time"));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(expirationTime.plusDays(1))) {
            long seconds = expirationTime.toEpochSecond(ZoneOffset.of("+8")) - now.toEpochSecond(ZoneOffset.of("+8"));
            redisUtil.setWithExpire("isReLogin:" + userId, true, seconds, TimeUnit.SECONDS);
            redisUtil.del(userId);
        }
    }

    public static void setUserUpdateInformation(RedisUtil redisUtil, String userId){
        Object cached = redisUtil.get(userId);
        if (cached instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) cached;
            String token = (String) map.get("token");
            if (token != null) {
                token = "Bearer " + token;
                HashMap<String, String> jwtMap = JSON.parseObject(JwtUtils.getSubject(token), HashMap.class);
                LocalDateTime expirationTime = LocalDateTime.parse(jwtMap.get("Expiration time"));
                LocalDateTime now = LocalDateTime.now();
                if (now.isBefore(expirationTime)) {
                    long seconds = expirationTime.toEpochSecond(ZoneOffset.of("+8")) - now.toEpochSecond(ZoneOffset.of("+8"));
                    redisUtil.setWithExpire("isUpdateInformation:" + userId, true, seconds, TimeUnit.SECONDS);
                    return;
                }
            }
        }
        // 兜底：Redis中无有效会话数据时，设置默认7天过期标记，确保Gateway下次请求时重新加载
        redisUtil.setWithExpire("isUpdateInformation:" + userId, true, 7, TimeUnit.DAYS);
    }
}
