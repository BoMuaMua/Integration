package com.example.permission.mapper;

import com.example.permission.entity.Activity;
import com.example.permission.entity.ActivityIndicators;
import com.example.permission.entity.AddPeople;
import com.example.permission.entity.Participants;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    // 添加活动与参与者的关联
    int addActivity(Participants participants);

    // 删除活动与参与者的关联
    int deleteActivityAssociation(@Param("activityId") Integer activityId);

    void addPeople(AddPeople addPeople);

    Integer deletePeople(@Param("activityId") Integer activityId, @Param("userIdList") List<Long> userIdList);

    Integer bindingIndicators(ActivityIndicators activityIndicators);

    Integer deleteBinding(Integer activityId, Integer indicatorsId);

    int deleteActivityIndicators(Integer activityId);
}
