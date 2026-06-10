package com.example.permission.service.impl;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.Roles;
import com.example.permission.entity.User;
import com.example.permission.entity.dto.RolesDTO;
import com.example.permission.mapper.PermissionGroupsMapper;
import com.example.permission.mapper.RolesMapper;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.RolesService;
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
public class RolesServiceImpl implements RolesService {

    @Resource
    private RolesMapper rolesMapper;

    @Override
    public List<Roles> getRolesList() {
        List<Roles> roles = (List<Roles>) redisUtil.get("roles");
        if (Objects.isNull(roles)) {
            QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
            roles = rolesMapper.selectList(queryWrapper);
            redisUtil.setWithExpire("roles", roles, 10, TimeUnit.MINUTES);
        }
        return roles;
    }

    @Override
    public ResponseResult addRoles(RolesDTO roles) {
        Roles newRoles = new Roles();
        newRoles.setName(roles.getName());
        if (rolesMapper.insert(newRoles) == 1) {
            removeRedis(null);
            return new ResponseResult(200, "添加成功");
        }
        return new ResponseResult(400, "添加失败");
    }

    @Override
    public ResponseResult deleteRoles(Integer roleId) {
        if (rolesMapper.deleteById(roleId) == 1) {
            updateUser(roleId);
            removeRedis(roleId);
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
    }

    @Override
    public ResponseResult updateRoles(Roles roles) {
        if (rolesMapper.updateById(roles) == 1) {
            updateUser(roles.getId());
            removeRedis(roles.getId());
            return new ResponseResult(200, "修改成功");
        }
        return new ResponseResult(400, "修改失败");
    }

    @Resource
    private PermissionGroupsMapper permissionGroupsMapper;

    @Override
    public ResponseResult getPermissionGroups(Integer roleId) {
        List<String> permissionGroups = permissionGroupsMapper.selectNameByRoleId(roleId);
        return new ResponseResult(200, "查询成功", permissionGroups);
    }

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    private void updateUser(Integer roleId) {
        if (roleId == null) {
            return;
        }
        List<String> userIds = userMapper.selectObjs(new QueryWrapper<User>().select("id").eq("role_id", roleId));
        if (!userIds.isEmpty()) {
            for (String userId : userIds) {
                SecurityUtils.setUserUpdateInformation(redisUtil, userId);
                log.info("用户id为{}下次登录自动更新信息", userId);
            }
        }
    }

    private void removeRedis(Integer roleId) {
        if (roleId != null) redisUtil.del("role:" + roleId);
        redisUtil.del("roles");
    }
}
