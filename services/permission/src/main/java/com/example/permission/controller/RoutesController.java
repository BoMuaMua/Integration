package com.example.permission.controller;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.Routes;
import com.example.permission.entity.User;
import com.example.permission.entity.dto.RoutesDTO;
import com.example.permission.service.RoutesService;
import com.example.permission.utils.UserInfoUtil;
import common.InfoUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "路由")
@RequestMapping("/routes")
public class RoutesController {
    @Resource
    private UserInfoUtil userInfoUtil;

    @Resource
    private RoutesService routesService;

    @Operation(summary = "获取路由列表")
    @GetMapping("/get")
    public ResponseResult getRoutesList() {
        return routesService.getRoutesList();
    }

    @Operation(summary = "添加路由")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody RoutesDTO routes) {
        return routesService.addRoutes(routes);
    }

    @Operation(summary = "删除路由")
    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestParam Long route_id) {
        return routesService.deleteRoutes(route_id);
    }

    @Operation(summary = "修改路由")
    @PutMapping("/update")
    public ResponseResult update(@RequestBody Routes routes) {
        return routesService.updateRoutes(routes);
    }

    @Operation(summary = "获取路由菜单")
    @GetMapping("/getMenus")
    public ResponseResult getMenusRoute(@RequestParam(required = false) Long parentId,@RequestParam Integer clientId){
        return routesService.getMenusRoute(parentId,clientId);
    }

    @Operation(summary = "获取个人某子系统路由菜单")
    @GetMapping("/getPersonalMenus")
    public ResponseResult getPersonalMenusRoute(HttpServletRequest request, @RequestParam Integer clientId){
        User user = userInfoUtil.getUserInfo(request.getHeader("X-USER-ID"));
        return routesService.getPersonalMenusRoute(user.getId(),clientId);
    }
}
