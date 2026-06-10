package com.example.permission.service.impl;

import com.example.permission.entity.DepartmentDetail;
import com.example.permission.entity.Departments;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.dto.DepartmentsDTO;
import com.example.permission.mapper.DepartmentsMapper;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.DepartmentsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.permission.utils.RedisUtil;
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
    public ResponseResult addDepartment(DepartmentsDTO departments) {
        Departments newDepartments = new Departments();
        newDepartments.setName(departments.getName());
        if (departmentsMapper.insert(newDepartments) == 1) {
            removeRedis(null);
            return new ResponseResult(200, "添加成功");
        }
        return new ResponseResult(400, "添加失败");
    }

    @Override
    public ResponseResult deleteDepartment(Integer departmentId) {
        if (departmentsMapper.deleteById(departmentId) == 1) {
            removeRedis(departmentId);
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
    }

    @Override
    public ResponseResult updateDepartment(Departments departments) {
        if (departmentsMapper.updateById(departments) == 1) {
            removeRedis(departments.getId());
            return new ResponseResult(200, "修改成功");
        }
        return new ResponseResult(400, "修改失败");
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
