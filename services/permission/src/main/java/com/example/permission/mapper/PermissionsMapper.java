package com.example.permission.mapper;

import com.example.permission.entity.Permissions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 1m1ng
 * @since 2024-03-03
 */
public interface PermissionsMapper extends BaseMapper<Permissions> {
    List<String> selectPermsByUserId(Long id);

    List<String> selectPermsByRoute(String path);

    List<Permissions> selectByGroupId(String groupId);
}
