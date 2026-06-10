package com.example.signin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.signin.entity.DepartmentDetail;
import com.example.signin.entity.Departments;
import com.example.signin.entity.ResponseResult;
import com.example.signin.mapper.DepartmentsMapper;
import com.example.signin.mapper.UserMapper;
import com.example.signin.service.DepartmentsService;
import com.example.signin.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class DepartmentsServiceImpl implements DepartmentsService {

    @Resource
    private DepartmentsMapper departmentsMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<Departments> getDepartmentsList() {
        List<Departments> departments = (List<Departments>) redisUtil.get("departments");
        if (Objects.isNull(departments)) {
            QueryWrapper<Departments> queryWrapper = new QueryWrapper<>();
            departments = departmentsMapper.selectList(queryWrapper);
            redisUtil.setWithExpire("departments", departments, 10, TimeUnit.MINUTES);
        }
        return departments;
    }

    @Override
    public ResponseResult getDepartmentDetail(Integer departmentId) {
         List<DepartmentDetail> departmentDetails =userMapper.selectDetailByDepartmentId(departmentId);
         return new ResponseResult(200, "获取成功", departmentDetails);
    }

    private void removeRedis(Integer departmentId) {
        if (departmentId != null) redisUtil.del("department:" + departmentId);
        redisUtil.del("departments");
    }
}
