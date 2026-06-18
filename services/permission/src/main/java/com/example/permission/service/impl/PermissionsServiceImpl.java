package com.example.permission.service.impl;

import com.example.permission.entity.Permissions;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PermissionsDTO;
import com.example.permission.mapper.PermissionsMapper;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.PermissionsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.permission.utils.RedisUtil;
import com.example.permission.utils.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PermissionsServiceImpl implements PermissionsService {

    @Resource
    private PermissionsMapper permissionsMapper;

    @Override
    public ResponseResult getPermissionList() {
        List<Permissions> permissions = (List<Permissions>) redisUtil.get("permissions");
        if (Objects.isNull(permissions)){
            QueryWrapper<Permissions> queryWrapper = new QueryWrapper<>();
            permissions = permissionsMapper.selectList(queryWrapper);
            redisUtil.setWithExpire("permissions", permissions, 5, TimeUnit.MINUTES);
        }
        return new ResponseResult(200, "查询成功", permissions);
    }

    @Override
    public ResponseResult addPermission(PermissionsDTO permissions) {
        Permissions newPermissions = new Permissions();
        newPermissions.setPerms(permissions.getPerms());
        newPermissions.setName(permissions.getName());
        if (permissionsMapper.insert(newPermissions) == 1) {
            redisUtil.del("permissions");
            return new ResponseResult(200, "添加成功");
        }
        return new ResponseResult(400, "添加失败");
    }

    @Override
    public ResponseResult deletePermission(String permissionId) {
        if (permissionsMapper.deleteById(permissionId) == 1) {
            updateUser(Long.parseLong(permissionId));
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
    }

    @Override
    public ResponseResult updatePermission(Permissions permissions) {
        if (permissionsMapper.updateById(permissions) == 1) {
            updateUser(permissions.getId());
            return new ResponseResult(200, "修改成功");
        }
        return new ResponseResult(400, "修改失败");
    }

    @Override
    public ResponseResult getByGroupId(String groupId) {
        List<Permissions> permissions = permissionsMapper.selectByGroupId(groupId);
        if (permissions.isEmpty()){
            return new ResponseResult(404, "未查询到数据");
        }
        return new ResponseResult(200, "查询成功", permissions);
    }

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        List<String> perms = permissionsMapper.selectPermsByUserId(userId);
        return perms != null ? perms : List.of();
    }

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    private void updateUser(Long permissionId) {
        if (permissionId == null) {
            return;
        }
        List<String> userIds = userMapper.selectIdByPermissionId(permissionId);
        if (!userIds.isEmpty()) {
            for (String userId : userIds) {
                SecurityUtils.setUserUpdateInformation(redisUtil, userId);
                log.info("用户id为{}下次登录自动更新信息", userId);
            }
        }
        redisUtil.del("permissions");
    }
}
