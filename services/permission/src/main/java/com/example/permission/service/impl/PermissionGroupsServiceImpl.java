package com.example.permission.service.impl;

import com.example.permission.entity.PermissionGroups;
import com.example.permission.entity.GroupsPermissions;
import com.example.permission.entity.Permissions;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.PermissionGroupsDTO;
import com.example.permission.mapper.GroupsPermissionsMapper;
import com.example.permission.mapper.PermissionGroupsMapper;
import com.example.permission.mapper.PermissionsMapper;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.PermissionGroupsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.permission.utils.RedisUtil;
import com.example.permission.utils.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PermissionGroupsServiceImpl implements PermissionGroupsService {

    @Resource
    private PermissionGroupsMapper permissionGroupsMapper;
    @Resource
    private GroupsPermissionsMapper groupsPermissionsMapper;
    @Resource
    private PermissionsMapper permissionsMapper;
    @Override
    public ResponseResult getPermissionGroupsList() {
        List<PermissionGroups> permissionGroups = (List<PermissionGroups>) redisUtil.get("permissionGroups");
        if (Objects.isNull(permissionGroups)) {
            QueryWrapper<PermissionGroups> queryWrapper = new QueryWrapper<>();
            permissionGroups = permissionGroupsMapper.selectList(queryWrapper);
            redisUtil.setWithExpire("permissionGroups", permissionGroups, 5, TimeUnit.MINUTES);
        }
        return new ResponseResult(200, "查询成功", permissionGroups);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addPermissionGroups(PermissionGroupsDTO permissionGroups) {
        try {
            PermissionGroups newPermissionGroups = new PermissionGroups();
            newPermissionGroups.setName(permissionGroups.getName());
            newPermissionGroups.setDescription(permissionGroups.getDescription());
            if (permissionGroupsMapper.insert(newPermissionGroups) == 1) {
                for (Permissions permission : permissionGroups.getPermissions()){
                    permissionsMapper.insert(permission);
                    groupsPermissionsMapper.insert(new GroupsPermissions(newPermissionGroups.getId(), Math.toIntExact(permission.getId())));
                }

                redisUtil.del("permissionGroups");
            }
            return new ResponseResult(200, "添加成功");
        }catch (Exception e) {
            throw new RuntimeException("删除失败，事务已回滚！", e);
        }
    }

    @Override
    public ResponseResult deletePermissionGroups(Integer permissionGroupsId) {
        if (permissionGroupsMapper.deleteById(permissionGroupsId) == 1) {
            updateUser(permissionGroupsId);
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
    }

    @Override
    public ResponseResult updatePermissionGroups(PermissionGroups permissionGroups) {
        if (permissionGroupsMapper.updateById(permissionGroups) == 1) {
            updateUser(permissionGroups.getId());
            return new ResponseResult(200, "修改成功");
        }
        return new ResponseResult(400, "修改失败");
    }

    @Override
    public ResponseResult addPermissionsToGroup(Integer groupId, List<Integer> permissionIds) {
        // 检查权限组是否存在
        PermissionGroups group = permissionGroupsMapper.selectById(groupId);
        if (group == null) {
            return new ResponseResult(400, "权限组不存在");
        }

        // 检查权限ID列表是否为空
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new ResponseResult(400, "权限ID列表不能为空");
        }

        // 将权限ID列表添加到权限组
        for (Integer permissionId : permissionIds) {
            // 假设有一个中间表 PermissionGroupsPermissions 来存储权限组和权限的关系
            GroupsPermissions groupsPermissions = new GroupsPermissions(groupId,permissionId);

            // 插入中间表
            if (groupsPermissionsMapper.insert(groupsPermissions) < 1) {
                return new ResponseResult(400, "添加权限失败");
            }
        }

        // 清除缓存
        redisUtil.del("permissionGroups");

        return new ResponseResult(200, "添加权限成功");
    }

    @Override
    public ResponseResult removePermissionsFromGroup(Integer groupId, List<Integer> permissionIds) {
        // 检查权限组是否存在
        PermissionGroups group = permissionGroupsMapper.selectById(groupId);
        if (group == null) {
            return new ResponseResult(400, "权限组不存在");
        }

        // 检查权限ID列表是否为空
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new ResponseResult(400, "权限ID列表不能为空");
        }

        // 删除权限组中的权限
        QueryWrapper<GroupsPermissions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId)
                .in("permission_id", permissionIds);

        int result = groupsPermissionsMapper.delete(queryWrapper);
        if (result > 0) {
            // 清除缓存
            redisUtil.del("permissionGroups");

            // 更新相关用户的权限信息
            updateUser(groupId);

            return new ResponseResult(200, "删除权限成功");
        } else {
            return new ResponseResult(400, "删除权限失败");
        }
    }



    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    private void updateUser(Integer groupId) {
        if (groupId == null) {
            return;
        }
        List<String> userIds = userMapper.selectIdByGroupId(groupId);
        if (!userIds.isEmpty()) {
            for (String userId : userIds) {
                SecurityUtils.setUserUpdateInformation(redisUtil, userId);
                log.info("用户id为{}下次登录自动更新信息", userId);
            }
        }
        redisUtil.del("permissionGroups");
    }
}
