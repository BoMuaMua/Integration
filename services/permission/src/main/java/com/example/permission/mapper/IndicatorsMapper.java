package com.example.permission.mapper;

import com.example.permission.entity.Indicators;
import com.example.permission.entity.IndicatorsMessage;
import com.example.permission.entity.UserScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IndicatorsMapper extends BaseMapper<Indicators> {
    List<Indicators> getIndicatorsByActivityId(Integer activityId);

    List<IndicatorsMessage> selectAll();

    void delete(Integer indicatorsId);

    List<IndicatorsMessage> selectById(Integer activityId);

    List<UserScore> getUserScores(Integer year);

    List<UserScore> getUserScoresByActivityId(Integer activityId);
}
