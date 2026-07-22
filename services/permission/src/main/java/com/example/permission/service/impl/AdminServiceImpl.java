package com.example.permission.service.impl;


import com.example.permission.entity.*;
import com.example.permission.entity.dto.AdminAddUserDTO;
import com.example.permission.entity.dto.AdminUpdateUserDTO;
import com.example.permission.entity.dto.InfoUserDTO;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.AdminService;
import com.example.permission.service.DepartmentsService;
import com.example.permission.service.RolesService;
import com.example.permission.utils.RedisUtil;
import com.example.permission.utils.SecurityUtils;
import com.example.permission.utils.UserInfoUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Resource
    private UserInfoUtil userInfoUtil;

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
        // 更新前查询旧用户，获取旧的部门ID
        User oldUser = userMapper.selectById(user.getId());
        Integer oldDeptId = oldUser != null ? oldUser.getDepartmentId() : null;

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setGrade(user.getGrade());
        newUser.setRoleId(user.getRoleId());
        newUser.setDepartmentId(user.getDepartmentId());
        RSA rsa = new RSA(null, publicKey);
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            newUser.setEmail(rsa.encryptBase64(user.getEmail(), KeyType.PublicKey));
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            newUser.setPhone(rsa.encryptBase64(user.getPhone(), KeyType.PublicKey));
        }
        newUser.setStatus(user.getStatus());

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", newUser.getId());

        if (userMapper.update(newUser, updateWrapper) == 1) {
            // 清除用户信息缓存
            userInfoUtil.clearUserCache(newUser.getId().toString());
            // 清除旧部门和新部门的成员缓存（部门变更时两者都需要清除）
            if (oldDeptId != null) {
                userInfoUtil.clearDeptUsersCache(oldDeptId);
            }
            if (!Objects.equals(oldDeptId, newUser.getDepartmentId())) {
                userInfoUtil.clearDeptUsersCache(newUser.getDepartmentId());
            }
            // 强制标记用户下次请求时从DB重新加载信息
            SecurityUtils.setUserUpdateInformation(redisUtil, newUser.getId().toString());
            // 从DB重新加载最新数据返回
            InfoUserDTO freshData = buildInfoUserDTO(userMapper.selectById(newUser.getId()));
            return new ResponseResult(200, "修改成功", freshData);
        }
        return new ResponseResult(400, "修改失败");
    }

    @Override
    public ResponseResult addUser(AdminAddUserDTO user) {
        User newUser = new User();

        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setDepartmentId(user.getDepartmentId());
        newUser.setGrade(user.getGrade());
        newUser.setRoleId(user.getRoleId());
        newUser.setStatus(user.getStatus() != null ? user.getStatus() : 1);

        RSA rsa = new RSA(null, publicKey);
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            newUser.setEmail(rsa.encryptBase64(user.getEmail(), KeyType.PublicKey));
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            newUser.setPhone(rsa.encryptBase64(user.getPhone(), KeyType.PublicKey));
        }

        if (userMapper.insert(newUser) == 1) {
            // 清除部门成员缓存，使下次查询加载最新数据
            userInfoUtil.clearDeptUsersCache(newUser.getDepartmentId());
            // 从DB重新加载最新数据返回
            InfoUserDTO freshData = buildInfoUserDTO(userMapper.selectById(newUser.getId()));
            return new ResponseResult(200, "添加成功", freshData);
        }
        return new ResponseResult(400, "添加失败");
    }

    @Override
    public ResponseResult deleteUser(String userId) {
        // 删除前先获取用户信息以清除部门缓存
        User user = userMapper.selectById(userId);
        if (userMapper.deleteById(userId) == 1) {
            // 删除用户会话缓存
            redisUtil.del(userId);
            // 清除用户信息缓存
            userInfoUtil.clearUserCache(userId);
            // 清除部门成员缓存
            if (user != null) {
                userInfoUtil.clearDeptUsersCache(user.getDepartmentId());
            }
            return new ResponseResult(200, "删除成功");
        }
        return new ResponseResult(400, "删除失败");
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
