package com.example.permission.service.impl;


import com.example.permission.entity.*;
import com.example.permission.entity.dto.InfoUserDTO;
import com.example.permission.entity.dto.PersonUpdateUserDTO;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.DepartmentsService;
import com.example.permission.service.RolesService;
import com.example.permission.service.UserService;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.permission.utils.RedisUtil;
import com.example.permission.utils.UserInfoUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoUtil userInfoUtil;

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
    public ResponseResult information(HttpServletRequest  request) {
        User user = userInfoUtil.getUserInfo(request.getHeader("X-USER-ID"));
        InfoUserDTO infoUserDTO = new InfoUserDTO();
        infoUserDTO.setId(user.getId());
        infoUserDTO.setUsername(user.getUsername());
        infoUserDTO.setGrade(user.getGrade());
        RSA rsa = new RSA(privateKey, null);
        if (user.getEmail() != null) {
            infoUserDTO.setEmail(rsa.decryptStr(user.getEmail(), KeyType.PrivateKey));
        }
        if (user.getPhone() != null) {
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
        System.out.println(new ResponseResult(200, "获取成功", infoUserDTO));
        return new ResponseResult(200, "获取成功", infoUserDTO);
    }

    @Override
    public ResponseResult update(HttpServletRequest request,PersonUpdateUserDTO user) {
        User newUser = userInfoUtil.getUserInfo(request.getHeader("X-USER-ID"));

        // 设置新的用户名和等级
        newUser.setUsername(user.getUsername());
        newUser.setGrade(user.getGrade());

        // 使用 RSA 加密邮箱和电话
        RSA rsa = new RSA(null, publicKey);

        if (user.getEmail() != null) {
            newUser.setEmail(rsa.encryptBase64(user.getEmail(), KeyType.PublicKey)); // 使用公钥加密邮箱
        }

        if (user.getPhone() != null) {
            newUser.setPhone(rsa.encryptBase64(user.getPhone(), KeyType.PublicKey)); // 使用公钥加密电话
        }

        // 更新用户信息
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", newUser.getId());
        CustomUserDetails userDetails = (CustomUserDetails) redisUtil.get(newUser.getId().toString());
        if (userMapper.update(newUser, updateWrapper) == 1) {
            userDetails.setUser(newUser);
            // 将更新后的用户信息保存到 Redis
            HashMap<String, String> map = new HashMap<>();
            map.put("userDetails", JSON.toJSONString(userDetails));
            redisUtil.set(newUser.getId().toString(), map);

            return new ResponseResult(200, "修改成功");
        }

        return new ResponseResult(401, "修改失败");
    }

    @Override
    public ResponseResult allInformation(Integer departmentId) {
        List<User> users = userInfoUtil.selectByDepartmentId(departmentId);
        List<InfoUserDTO> infoUserDTOList = new ArrayList<>();

        RSA rsa = new RSA(privateKey, null);
        List<Departments> departments = departmentService.getDepartmentsList();
        List<Roles> roles = rolesService.getRolesList();

        for (User user : users) {
            InfoUserDTO infoUserDTO = new InfoUserDTO();
            infoUserDTO.setId(user.getId());
            infoUserDTO.setUsername(user.getUsername());
            infoUserDTO.setGrade(user.getGrade());

            if (user.getEmail() != null) {
                infoUserDTO.setEmail(rsa.decryptStr(user.getEmail(), KeyType.PrivateKey));
            }
            if (user.getPhone() != null) {
                infoUserDTO.setPhone(rsa.decryptStr(user.getPhone(), KeyType.PrivateKey));
            }

            infoUserDTO.setCreateTime(user.getCreateTime());
            infoUserDTO.setAvatar(user.getAvatar());
            infoUserDTO.setStatus(user.getStatus());

            for (Departments department : departments) {
                if (department.getId().equals(user.getDepartmentId())) {
                    infoUserDTO.setDepartmentId(department.getId());
                    infoUserDTO.setDepartment(department.getName());
                    break;
                }
            }

            for (Roles role : roles) {
                if (role.getId().equals(user.getRoleId())) {
                    infoUserDTO.setRoleId(role.getId());
                    infoUserDTO.setRole(role.getName());
                    break;
                }
            }

            infoUserDTOList.add(infoUserDTO);
        }

        System.out.println(new ResponseResult(200, "获取成功", infoUserDTOList));
        return new ResponseResult(200, "获取成功", infoUserDTOList);
    }

}






