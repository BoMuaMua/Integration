package com.example.signin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.signin.entity.SigninUser;
import com.example.signin.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper

public interface SigninUserMapper extends BaseMapper<SigninUser> {
    void insert(User user, String signinTime, String activityName);
    List<SigninUser> selectList(String activityName);
    int selectCount(String activityName);
    int insert(User user, String activityName);
    Integer selectStatusByIdAndActivityName(Long id, String activityName);
    void updateByIdAndActivityName(Long id, String signinTime,String activityName);
    ArrayList<SigninUser> selectSignUserByActivityName(String activityName);
    void delateActivity(String activityName);

    void updateSigninUserActivityName(String oldActivityName,String newActivityName);

    List<SigninUser> selectAll();
}
