package com.example.permission.service;


import com.example.permission.entity.*;
import org.springframework.transaction.annotation.Transactional;

public interface ActivityService {
    ResponseResult addActivity(Participants participants);

    @Transactional(rollbackFor = Exception.class)  // 确保所有异常都会回滚事务
    ResponseResult deleteActivity(Integer activityId);

    @Transactional(rollbackFor = Exception.class)  // 确保所有异常都会回滚事务
    ResponseResult updateActivity(Participants participants);

    @Transactional(rollbackFor = Exception.class)
    ResponseResult addPeople(AddPeople addPeople);

    @Transactional(rollbackFor = Exception.class)  // 保证在发生异常时进行回滚
    ResponseResult deletePeople(AddPeople addPeople);
    @Transactional(rollbackFor = Exception.class)  // 保证在发生异常时进行回滚
    ResponseResult bindingIndicators(ActivityIndicators activityIndicators);
    @Transactional(rollbackFor = Exception.class)
    ResponseResult deleteBinding(Integer activityId, Integer indicatorsId);

    ResponseResult score(UserFraction userFraction);

    ResponseResult list();
}
