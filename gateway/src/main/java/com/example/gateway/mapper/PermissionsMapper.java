package com.example.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gateway.entity.Permissions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionsMapper extends BaseMapper<Permissions> {
    List<String> selectPermsByUserId(Long id);
    List<String> selectPermsByRoute(String path);
}