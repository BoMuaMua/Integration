package com.example.check.utils;

import com.example.check.entity.pojo.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.check.entity.pojo.InfoUser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class RequestUtil {
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    private RedisUtil redisUtil;

    public List<InfoUser> getUserInfoList(String departmentId) {
        ServiceInstance instance = loadBalancerClient.choose("permission");
        //请求url，添加departmentId作为查询参数
        String url = "http://"+instance.getHost() +":" + instance.getPort()+ "/user/allInformation?departmentId=" + departmentId;
        //请求头
        HttpHeaders headers = new HttpHeaders();
        // 创建请求实体
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = restTemplate.exchange(url, HttpMethod.GET, entity, ResponseResult.class).getBody();

        Object data = response.getData();

        // 使用 ObjectMapper 进行正确的类型转换
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<InfoUser> userInfoList = mapper.convertValue(data, new TypeReference<List<InfoUser>>() {});

        return userInfoList;
    }
}