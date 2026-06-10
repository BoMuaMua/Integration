package com.example.signin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.signin.entity.ActivityType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface ActivityTypeMapper extends BaseMapper<ActivityType> {
    String getActivityData(String activityName);
    String checkByActivityType(String activityName);
    String selectTypeName(String activityName, Integer activityId);
}
