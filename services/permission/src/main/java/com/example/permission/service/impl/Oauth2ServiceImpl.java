package com.example.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.permission.entity.AccessToken;
import com.example.permission.entity.Oauth2CodeToToken;
import com.example.permission.entity.Oauth2RegisteredClient;
import com.example.permission.entity.ResponseResult;
import com.example.permission.mapper.Oauth2RegisteredClientMapper;
import com.example.permission.service.Oauth2Service;
import com.example.permission.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Oauth2ServiceImpl implements Oauth2Service {

    @Resource
    private Oauth2RegisteredClientMapper oauth2RegisteredClientMapper;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public ResponseResult redirect(String clientId, String id) {
        return null;
    }

    @Override
    public ResponseResult token(Oauth2CodeToToken oauth2CodeToToken) {
        return null;
    }

    @Override
    public ResponseResult data(AccessToken accessToken) throws JsonProcessingException {
        return null;
    }

    @Override
    public ResponseResult selectList() {
        // 先从 Redis 获取缓存
        List<Oauth2RegisteredClient> clients = (List<Oauth2RegisteredClient>) redisUtil.get("oauth2_clients");
        if (Objects.isNull(clients)) {
            QueryWrapper<Oauth2RegisteredClient> queryWrapper = new QueryWrapper<>();
            clients = oauth2RegisteredClientMapper.selectList(queryWrapper);
            // 缓存5分钟
            redisUtil.setWithExpire("oauth2_clients", clients, 5, TimeUnit.MINUTES);
        }
        return new ResponseResult(200, "查询成功", clients);
    }

    @Override
    public ResponseResult add(Oauth2RegisteredClient oauth2RegisteredClient) {
        // 检查 clientId 是否已存在
        QueryWrapper<Oauth2RegisteredClient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", oauth2RegisteredClient.getClientId());
        Oauth2RegisteredClient existClient = oauth2RegisteredClientMapper.selectOne(queryWrapper);
        if (existClient != null) {
            return new ResponseResult(400, "客户端ID已存在");
        }

        if (oauth2RegisteredClientMapper.insert(oauth2RegisteredClient) == 1) {
            // 清除缓存
            redisUtil.del("oauth2_clients");
            return new ResponseResult(200, "添加成功");
        }
        return new ResponseResult(400, "添加失败");
    }

    @Override
    public ResponseResult delete(Long id) {
        if (oauth2RegisteredClientMapper.deleteById(id) == 1) {
            // 清除缓存
            redisUtil.del("oauth2_clients");
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
    }

    @Override
    public ResponseResult update(Oauth2RegisteredClient oauth2RegisteredClient) {
        if (oauth2RegisteredClientMapper.updateById(oauth2RegisteredClient) == 1) {
            // 清除缓存
            redisUtil.del("oauth2_clients");
            return new ResponseResult(200, "修改成功");
        }
        return new ResponseResult(400, "修改失败");
    }

    @Override
    public ResponseResult selectById(Long id) {
        Oauth2RegisteredClient client = oauth2RegisteredClientMapper.selectById(id);
        if (client != null) {
            return new ResponseResult(200, "查询成功", client);
        }
        return new ResponseResult(404, "未找到该客户端");
    }

    @Override
    public ResponseResult disable(Long id) {
        Oauth2RegisteredClient client = new Oauth2RegisteredClient();
        client.setId(id.intValue());
        client.setStatus(0); // 0表示禁用

        if (oauth2RegisteredClientMapper.updateById(client) == 1) {
            // 清除缓存
            redisUtil.del("oauth2_clients");
            return new ResponseResult(200, "禁用成功");
        }
        return new ResponseResult(400, "禁用失败");
    }

    @Override
    public ResponseResult enable(Long id) {
        Oauth2RegisteredClient client = new Oauth2RegisteredClient();
        client.setId(id.intValue());
        client.setStatus(1); // 1表示启用

        if (oauth2RegisteredClientMapper.updateById(client) == 1) {
            // 清除缓存
            redisUtil.del("oauth2_clients");
            return new ResponseResult(200, "启用成功");
        }
        return new ResponseResult(400, "启用失败");
    }
}
