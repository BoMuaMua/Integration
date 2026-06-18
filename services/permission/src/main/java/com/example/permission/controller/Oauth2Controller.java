package com.example.permission.controller;

import com.example.permission.entity.Oauth2RegisteredClient;
import com.example.permission.entity.ResponseResult;
import com.example.permission.service.Oauth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "OAuth2客户端管理")
@RequestMapping("/oauth")
public class Oauth2Controller {

    @Resource
    private Oauth2Service oauth2Service;

    @Operation(summary = "查询所有子系统")
    @GetMapping("/selectList")
    public ResponseResult selectList() {
        return oauth2Service.selectList();
    }

    @Operation(summary = "添加子系统")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Oauth2RegisteredClient oauth2RegisteredClient) {
        return oauth2Service.add(oauth2RegisteredClient);
    }

    @Operation(summary = "删除子系统")
    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestParam Long id) {
        return oauth2Service.delete(id);
    }

    @Operation(summary = "修改子系统")
    @PutMapping("/update")
    public ResponseResult update(@RequestBody Oauth2RegisteredClient oauth2RegisteredClient) {
        return oauth2Service.update(oauth2RegisteredClient);
    }

    @Operation(summary = "根据id查询子系统")
    @GetMapping("/selectById")
    public ResponseResult selectById(@RequestParam Long id) {
        return oauth2Service.selectById(id);
    }

    @Operation(summary = "禁用子系统")
    @PutMapping("/disable")
    public ResponseResult disable(@RequestParam Long id) {
        return oauth2Service.disable(id);
    }

    @Operation(summary = "启用子系统")
    @PutMapping("/enable")
    public ResponseResult enable(@RequestParam Long id) {
        return oauth2Service.enable(id);
    }
}
