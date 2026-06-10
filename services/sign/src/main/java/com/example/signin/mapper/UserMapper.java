package com.example.signin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.signin.entity.ActivityList;
import com.example.signin.entity.DepartmentDetail;
import com.example.signin.entity.SigninUser;
import com.example.signin.entity.User;
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

    List<SigninUser> selectAlreadySignin(String activityName);

    List<SigninUser> selectByNotSignin(String activityName);

    int selectAlreadyCount(String activityName);

    List<String> selectIdByPermissionId(Long permissionId);

    List<String> selectIdByGroupId(Integer groupId);

    List<DepartmentDetail> selectDetailByDepartmentId(Integer departmentId);

    Integer selectNeedSignin(String activityName);


    Integer selectSigned(String activityName);

    String selectState(String userName, Long id, String activityName);

    Integer selectDepartmentId(String userName, Long id, String activityName);

    Integer selectNotSigned(String activityName);

//    计算某个活动已经签到人数和未签人数
    ActivityList countByActivityName(String activityName);
}
