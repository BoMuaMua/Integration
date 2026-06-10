package com.example.permission.service.impl;


import com.example.permission.entity.*;
import com.example.permission.entity.dto.RoutesDTO;
import com.example.permission.mapper.PermissionsMapper;
import com.example.permission.mapper.RoutesMapper;
import com.example.permission.service.RoutesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.permission.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RoutesServiceImpl implements RoutesService {

    @Resource
    private RoutesMapper routesMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private PermissionsMapper permissionsMapper;

    @Override
    public ResponseResult getRoutesList() {
        List<Routes> routes = (List<Routes>) redisUtil.get("routes");
        if (Objects.isNull(routes)) {
            QueryWrapper<Routes> queryWrapper = new QueryWrapper<>();
            routes = routesMapper.selectList(queryWrapper);
            redisUtil.setWithExpire("routes", routes, 5, TimeUnit.MINUTES);
        }
        return new ResponseResult(200, "查询成功", routes);
    }

    @Override
    public ResponseResult addRoutes(RoutesDTO route) {
        Routes newRoute = new Routes();
        newRoute.setName(route.getName());
        newRoute.setPath(route.getPath());
        newRoute.setComponent(route.getComponent());
        newRoute.setIcon(route.getIcon());
        newRoute.setParent(route.getParent());
        newRoute.setPerm(route.getPerm());

        if (routesMapper.insert(newRoute) == 1) {
            removeRedis(null);
            return new ResponseResult(200, "添加成功");
        }
        return new ResponseResult(400, "添加失败");
    }

    @Override
    public ResponseResult deleteRoutes(Long route_id) {
        if (routesMapper.deleteById(route_id) == 1) {
            removeRedis(route_id);
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
    }

    @Override
    public ResponseResult updateRoutes(Routes routes) {
        if (routesMapper.updateById(routes) == 1) {
            removeRedis(routes.getId());
            return new ResponseResult(200, "修改成功");
        }
        return new ResponseResult(400, "修改失败");
    }

    @Override
    public ResponseResult getMenusRoute(Long parentId, Integer clientId) {
        List<RoutesMenu> menuRoutes;
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> authorityNames = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            authorityNames.add(authority.getAuthority());
        }

        QueryWrapper<Permissions> queryWrapper = new QueryWrapper<Permissions>().in("perms", authorityNames);
        queryWrapper.select("perms");
        List<Long> permissionIds = permissionsMapper.selectObjs(queryWrapper);

        List<Routes> routes = new ArrayList<>();
        if (!permissionIds.isEmpty()) {
            QueryWrapper<Routes> routesQueryWrapper = new QueryWrapper<Routes>().in("perm", permissionIds);
            routesQueryWrapper.eq("client_id", clientId);
            routes = routesMapper.selectList(routesQueryWrapper);
        }

        menuRoutes = getMenuRoutes(routes, parentId);
        return new ResponseResult(200, "查询成功", menuRoutes);
    }

    @Override
    public ResponseResult getPersonalMenusRoute(Long id, Integer clientId) {
        List<Routes> routes = routesMapper.selectUserRoutes(id, clientId);
        return new ResponseResult(200, "查询成功", routes);
    }

    private List<RoutesMenu> getMenuRoutes(List<Routes> routes, Long parentId) {
        List<RoutesMenu> routeMenus = new ArrayList<>();
        for (Routes route : routes) {
            if ((route.getParent() == null && parentId == null) || (route.getParent() != null && route.getParent().equals(parentId))) {
                RoutesMenu menuRoute = new RoutesMenu();
                menuRoute.setName(route.getName());
                menuRoute.setComponent(route.getComponent());
                menuRoute.setPath(route.getPath());
                menuRoute.setIcon(route.getIcon());
                Meta meta = new Meta();
                meta.setTitle(route.getName());
                menuRoute.setMeta(meta);
                menuRoute.setChildren(getMenuRoutes(routes, route.getId()));
                routeMenus.add(menuRoute);
            }
        }
        return routeMenus;
    }

    private void removeRedis(Long routeId) {
        redisUtil.del("routes");
        if (routeId != null) {
            redisUtil.del("menuRoutes:" + routeId);
        }
    }
}
