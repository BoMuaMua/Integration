package com.example.permission.service;

import com.example.permission.entity.IndicatorsMessage;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.deleteIndicators;

public interface IndicatorsService {
    ResponseResult get();

    ResponseResult add(IndicatorsMessage indicatorsMessage);

    ResponseResult update(IndicatorsMessage indicatorsMessage);

    ResponseResult delete(deleteIndicators deleteIndicators);

    ResponseResult getIndicatorsByActivityId(Integer activityId);

    ResponseResult getUserIndicators(Integer year);

    ResponseResult getUserIndicatorsByActivityId(Integer activityId);
}
