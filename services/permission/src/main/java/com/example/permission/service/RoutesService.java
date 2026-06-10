package com.example.permission.service;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.Routes;
import com.example.permission.entity.dto.RoutesDTO;

public interface RoutesService {
    ResponseResult getRoutesList();

    ResponseResult addRoutes(RoutesDTO route);

    ResponseResult deleteRoutes(Long route_id);

    ResponseResult updateRoutes(Routes routes);

    ResponseResult getMenusRoute(Long parentId,Integer clientId);

    ResponseResult getPersonalMenusRoute(Long id, Integer clientId);
}
