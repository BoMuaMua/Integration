package com.example.permission.service.impl;


import com.example.permission.entity.*;
import com.example.permission.mapper.ElementsMapper;
import com.example.permission.mapper.IndicatorsMapper;
import com.example.permission.service.IndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IndicatorsServiceImpl implements IndicatorsService {

    private IndicatorsMapper indicatorsMapper;
    @Autowired
    public IndicatorsServiceImpl(IndicatorsMapper indicatorsMapper, ElementsMapper elementsMapper) {
        this.indicatorsMapper = indicatorsMapper;
        this.elementsMapper = elementsMapper;
    }

    private ElementsMapper elementsMapper;


    @Override
    public ResponseResult get() {
        List<IndicatorsMessage> indicators = indicatorsMapper.selectAll();
        return new ResponseResult(200, "查询成功", indicators);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult add(IndicatorsMessage indicatorsMessage) {
        try {
            int sum = indicatorsMessage.getElements().stream()
                    .mapToInt(Elements::getStandardScore)
                    .sum();
            if (sum != 10){
                return new ResponseResult(400, "标准分之和不为10分，请检查！");
            }
            // 1. 先插入主表
            Indicators indicators = new Indicators();
            indicators.setIndicatorsName(indicatorsMessage.getIndicatorsName());
            try {
                indicatorsMapper.insert(indicators);
            }catch (DataIntegrityViolationException e){
                return new ResponseResult(400, "名字重复了，请重新输入");
            }
             // 插入后 MyBatis 会自动填充 indicatorsId

            // 2. 获取数据库生成的主键
            Integer indicatorsId = indicators.getIndicatorsId();
            if (indicatorsId == null) {
                throw new RuntimeException("获取 indicatorsId 失败，事务回滚！");
            }

            // 3. 确保 elements 列表不为空，并插入数据
            if (indicatorsMessage.getElements() != null && !indicatorsMessage.getElements().isEmpty()) {
                for (Elements element : indicatorsMessage.getElements()) {
                    element.setIndicatorsId(indicatorsId); // 绑定外键
                }
                elementsMapper.insert(indicatorsMessage.getElements()); // 批量插入
            }

            return new ResponseResult(200, "添加成功");
        } catch (Exception e) {
            throw new RuntimeException("添加失败，事务已回滚！", e); // **手动抛出异常，触发回滚**
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult update(IndicatorsMessage indicatorsMessage) {
        try {
            int sum = indicatorsMessage.getElements().stream()
                    .mapToInt(Elements::getStandardScore)
                    .sum();
            if (sum != 10){
                return new ResponseResult(400, "标准分之和不为10分，请检查！");
            }

            // 1. 先插入主表
            Indicators indicators = new Indicators();
            indicators.setIndicatorsId(indicatorsMessage.getIndicatorsId());
            indicators.setIndicatorsName(indicatorsMessage.getIndicatorsName());
            indicatorsMapper.updateById(indicators);

            // 3. 确保 elements 列表不为空，并插入数据
            if (indicatorsMessage.getElements() != null && !indicatorsMessage.getElements().isEmpty()) {
                elementsMapper.updateAndInsert(indicatorsMessage.getElements()); // 批量插入
            }

            return new ResponseResult(200, "添加成功");
        } catch (Exception e) {
            throw new RuntimeException("添加失败，事务已回滚！", e); // **手动抛出异常，触发回滚**
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(deleteIndicators deleteIndicators) {
        try {
            List<Integer> elementsId = deleteIndicators.getElementsId();
            if (elementsId != null){
                for (Integer elementId : elementsId) {
                    elementsMapper.delete(elementId);
                }
            }
            elementsMapper.deleteByIndicatorsId(deleteIndicators.getIndicatorsId());
            indicatorsMapper.deleteById(deleteIndicators.getIndicatorsId());
            return new ResponseResult(200, "删除成功");
        } catch (Exception e){
            throw new RuntimeException("删除失败，事务已回滚！", e);
        }
    }

    @Override
    public ResponseResult getIndicatorsByActivityId(Integer activityId) {
        List<IndicatorsMessage> indicators = indicatorsMapper.selectById(activityId);
        return new ResponseResult(200, "查询成功", indicators);
    }

    @Override
    public ResponseResult getUserIndicators(Integer year) {
        List<UserScore> userScores = indicatorsMapper.getUserScores(year);
        for (UserScore userScore : userScores){
            if (userScore.getTotalScore() != null) {
                userScore.setStatus("已评价");
            } else {
                userScore.setStatus("未评价");
            }
        }
        return new ResponseResult(200, "查询成功", userScores);
    }

    @Override
    public ResponseResult getUserIndicatorsByActivityId(Integer activityId) {
        List<UserScore> userScores = indicatorsMapper.getUserScoresByActivityId(activityId);
        for (UserScore userScore : userScores){
            if (userScore.getTotalScore() != null) {
                userScore.setStatus("已评价");
            } else {
                userScore.setStatus("未评价");
            }
        }
        return new ResponseResult(200, "查询成功", userScores);
    }

}
