package com.example.permission.service.impl;


import com.example.permission.entity.*;
import com.example.permission.entity.dto.AdminUpdateUserDTO;
import com.example.permission.entity.dto.InfoUserDTO;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.AdminService;
import com.example.permission.service.DepartmentsService;
import com.example.permission.service.RolesService;
import com.example.permission.utils.RedisUtil;
import com.example.permission.utils.SecurityUtils;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    @Value("${rsa.privateKey}")
    private String privateKey;

    @Value("${rsa.publicKey}")
    private String publicKey;

    @Resource
    private DepartmentsService departmentService;

    @Resource
    private RolesService rolesService;

    @Override
    public ResponseResult informationUser() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        List<InfoUserDTO> infoUserDTOList = new ArrayList<>();
        RSA rsa = new RSA(privateKey, null);
        for (User user : userList) {
            InfoUserDTO infoUserDTO = new InfoUserDTO();
            infoUserDTO.setId(user.getId());
            infoUserDTO.setUsername(user.getUsername());
            infoUserDTO.setGrade(user.getGrade());
            if (!Objects.isNull(user.getEmail()) && !user.getEmail().isEmpty()) {
                infoUserDTO.setEmail(rsa.decryptStr(user.getEmail(), KeyType.PrivateKey));
            }
            if (!Objects.isNull(user.getPhone()) && !user.getPhone().isEmpty()) {
                infoUserDTO.setPhone(rsa.decryptStr(user.getPhone(), KeyType.PrivateKey));
            }
            infoUserDTO.setCreateTime(user.getCreateTime());
            infoUserDTO.setAvatar(user.getAvatar());
            infoUserDTO.setStatus(user.getStatus());
            List<Departments> departments = departmentService.getDepartmentsList();
            for (Departments department : departments) {
                if (department.getId().equals(user.getDepartmentId())) {
                    infoUserDTO.setDepartmentId(department.getId());
                    infoUserDTO.setDepartment(department.getName());
                    break;
                }
            }
            List<Roles> roles = rolesService.getRolesList();
            for (Roles role : roles) {
                if (role.getId().equals(user.getRoleId())) {
                    infoUserDTO.setRoleId(role.getId());
                    infoUserDTO.setRole(role.getName());
                    break;
                }
            }
            infoUserDTOList.add(infoUserDTO);
        }
        return new ResponseResult(200, "查询成功", infoUserDTOList);
    }

    @Override
    public ResponseResult updateUser(AdminUpdateUserDTO user) {
        User newUser = new User();

        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setGrade(user.getGrade());
        newUser.setRoleId(user.getRoleId());
        newUser.setDepartmentId(user.getDepartmentId());
        RSA rsa = new RSA(null, publicKey);
//        InformationSupplementController.setUserDetails(newUser, rsa, user.getEmail(), user.getPhone());
        newUser.setStatus(user.getStatus());

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", newUser.getId());

        if (userMapper.update(newUser, updateWrapper) == 1) {
            HashMap<String, String> map = (HashMap<String, String>) redisUtil.get(newUser.getId().toString());
            if (map != null) {
                CustomUserDetails userDetails = JSON.parseObject(map.get("userDetails"), CustomUserDetails.class);
                if (!user.getRoleId().equals(userDetails.getUser().getRoleId())) {
                    SecurityUtils.setUserUpdateInformation(redisUtil, newUser.getId().toString());
                } else {
                    userDetails.setUser(newUser);
                    map = (HashMap<String, String>) redisUtil.get(newUser.getId().toString());
                    map.put("userDetails", JSON.toJSONString(userDetails));
                    redisUtil.set(newUser.getId().toString(), map);
                }
            }
            return new ResponseResult(200, "修改成功");
        }
        return new ResponseResult(400, "修改失败");
    }

    @Override
    public ResponseResult deleteUser(String userId) {
        if (userMapper.deleteById(userId) == 1) {
            // 删除用户缓存
            redisUtil.del(userId);
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
    }
}
