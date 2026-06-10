package com.example.permission.mapper;

import com.example.permission.entity.DepartmentDetail;
import com.example.permission.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 1m1ng
 * @since 2024-03-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> selectByDepartmentId(Integer departmentId);

    List<User> selectByDepartmentIdAndRoleId(Integer departmentId);

    User selectByIdAndRoleId(Long userId);

    int selectAlreadyCount(String activityName);

    List<String> selectIdByPermissionId(Long permissionId);

    List<String> selectIdByGroupId(Integer groupId);

    List<DepartmentDetail> selectDetailByDepartmentId(Integer departmentId);

    Integer selectNeedSignin(String activityName);


    Integer selectSigned(String activityName);

    String selectState(String userName, Long id, String activityName);

    Integer selectDepartmentId(String userName, Long id, String activityName);

    Integer selectNotSigned(String activityName);

}
