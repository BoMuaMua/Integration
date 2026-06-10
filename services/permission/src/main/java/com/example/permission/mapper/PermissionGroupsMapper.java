package com.example.permission.mapper;

import com.example.permission.entity.PermissionGroups;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 1m1ng
 * @since 2024-03-10
 */
public interface PermissionGroupsMapper extends BaseMapper<PermissionGroups> {

    List<String> selectNameByRoleId(Integer roleId);
}
