package com.example.signin.utils;

import com.example.signin.entity.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import common.InfoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Component
public class RequestUtil {
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    private RedisUtil redisUtil;

    public InfoUser getUserInfo(String userId) {
        if (redisUtil.get(userId) != null){
            return (InfoUser) redisUtil.get(userId);
        }else {
            ServiceInstance instance = loadBalancerClient.choose("permission");
            String url = "http://"+instance.getHost() +":" + instance.getPort()+ "/user/information";
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId);
            // 创建请求实体
            HttpEntity<?> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseResult response = restTemplate.exchange(url, HttpMethod.GET, entity, ResponseResult.class).getBody();
            System.out.println(response);
            Object data = response.getData();

            // 使用 ObjectMapper 进行类型转换
            ObjectMapper mapper = new ObjectMapper();
            InfoUser userInfo = mapper.convertValue(data, InfoUser.class);

            // 缓存到Redis
            redisUtil.setWithExpire(userId, userInfo, 240 * 60, TimeUnit.SECONDS);
            return userInfo;
        }
    }
}
