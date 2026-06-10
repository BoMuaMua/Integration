package com.example.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gateway.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
