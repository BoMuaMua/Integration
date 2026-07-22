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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.permission.utils.RedisUtil;
import com.example.permission.utils.SecurityUtils;
import com.example.permission.utils.UserInfoUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if (userMapper.update(newUser, updateWrapper) == 1) {
            // 清除用户信息缓存
            userInfoUtil.clearUserCache(newUser.getId().toString());
            // 清除部门成员缓存
            userInfoUtil.clearDeptUsersCache(newUser.getDepartmentId());
            // 强制标记用户下次请求时从DB重新加载信息
            SecurityUtils.setUserUpdateInformation(redisUtil, newUser.getId().toString());
            // 从DB重新加载最新数据返回（绕过缓存）
            User freshUser = userMapper.selectById(newUser.getId());
            InfoUserDTO freshData = buildInfoUserDTO(freshUser);
            return new ResponseResult(200, "修改成功", freshData);
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

    /**
     * 构建InfoUserDTO（解密邮箱/手机号，填充部门/角色名称）
     */
    private InfoUserDTO buildInfoUserDTO(User user) {
        if (user == null) {
            return null;
        }
        InfoUserDTO dto = new InfoUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setGrade(user.getGrade());
        RSA rsa = new RSA(privateKey, null);
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            dto.setEmail(rsa.decryptStr(user.getEmail(), KeyType.PrivateKey));
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            dto.setPhone(rsa.decryptStr(user.getPhone(), KeyType.PrivateKey));
        }
        dto.setCreateTime(user.getCreateTime());
        dto.setAvatar(user.getAvatar());
        dto.setStatus(user.getStatus());
        dto.setDepartmentId(user.getDepartmentId());
        dto.setRoleId(user.getRoleId());

        List<Departments> departments = departmentService.getDepartmentsList();
        for (Departments department : departments) {
            if (department.getId().equals(user.getDepartmentId())) {
                dto.setDepartment(department.getName());
                break;
            }
        }
        List<Roles> roles = rolesService.getRolesList();
        for (Roles role : roles) {
            if (role.getId().equals(user.getRoleId())) {
                dto.setRole(role.getName());
                break;
            }
        }
        return dto;
    }
}
