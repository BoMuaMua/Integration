package com.example.permission.service;

import com.example.permission.entity.AccessToken;
import com.example.permission.entity.Oauth2CodeToToken;
import com.example.permission.entity.Oauth2RegisteredClient;
import com.example.permission.entity.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface Oauth2Service {
    ResponseResult redirect(String clientId,String id);

    ResponseResult token(Oauth2CodeToToken oauth2CodeToToken);

    ResponseResult data(AccessToken accessToken) throws JsonProcessingException;

    ResponseResult selectList();

    ResponseResult add(Oauth2RegisteredClient oauth2RegisteredClient);

    ResponseResult delete(Long id);

    ResponseResult update(Oauth2RegisteredClient oauth2RegisteredClient);

    ResponseResult selectById(Long id);

    ResponseResult disable(Long id);

    ResponseResult enable(Long id);
}