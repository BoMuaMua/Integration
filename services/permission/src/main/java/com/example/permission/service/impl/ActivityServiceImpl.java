package com.example.permission.service.impl;


import com.example.permission.entity.*;
import com.example.permission.mapper.ActivityMapper;
import com.example.permission.mapper.UserTemporaryMapper;
import com.example.permission.service.ActivityService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private UserTemporaryMapper userTemporaryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)  // 确保所有异常都会回滚事务
    public ResponseResult addActivity(Participants participants) {
        try {
            int activityInsertResult = activityMapper.insert(participants.getActivity());

            if (activityInsertResult == 0) {
                // 如果插入活动失败，抛出自定义异常
                return new ResponseResult(400, "活动添加失败，插入活动时发生错误");
            }

            int addActivityResult = activityMapper.addActivity(participants);

            if (addActivityResult == 0) {
                // 如果关联操作失败，抛出自定义异常
                return new ResponseResult(400, "活动和参与者关联失败或未添加参与活动的人员");
            }

            return new ResponseResult(200, "添加成功");

        }catch (Exception e) {
            // 捕获其他异常并返回通用错误信息
            return new ResponseResult(500, "服务器内部错误: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 确保所有异常都会回滚事务
    public ResponseResult deleteActivity(Integer activityId) {
        try {
            // 先删除活动和参与者的关联
            int deleteAssociationResult = activityMapper.deleteActivityAssociation(activityId);

            if (deleteAssociationResult == 0) {
                // 如果删除关联失败
                return new ResponseResult(400, "活动和参与者的关联删除失败或未检测到活动参加人员");
            }

            int deleteIndicatorsResult = activityMapper.deleteActivityIndicators(activityId);
            if (deleteIndicatorsResult == 0) {
                // 如果删除关联失败
                return new ResponseResult(400, "活动和指标的关联删除失败或未检测到活动参加人员");
            }

            // 再删除活动
            int deleteActivityResult = activityMapper.deleteById(activityId);

            if (deleteActivityResult == 0) {
                // 如果删除活动失败
                return new ResponseResult(400, "活动删除失败");
            }

            return new ResponseResult(200, "删除成功");

        } catch (Exception e) {
            // 捕获其他异常并返回通用错误信息
            return new ResponseResult(500, "服务器内部错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 确保所有异常都会回滚事务
    public ResponseResult updateActivity(Participants participants) {
        try {
            // 更新活动信息
            int updateActivityResult = activityMapper.updateById(participants.getActivity());

            if (updateActivityResult == 0) {
                // 如果更新活动信息失败
                return new ResponseResult(400, "活动更新失败");
            }

            int updateAssociationResult = activityMapper.deleteActivityAssociation(participants.getActivity().getActivityId());
            int addAssociationResult = activityMapper.addActivity(participants);

            if (updateAssociationResult == 0 || addAssociationResult == 0) {
                // 如果更新关联失败
                return new ResponseResult(400, "活动和参与者关联更新失败");
            }

            return new ResponseResult(200, "更新成功");

        } catch (Exception e) {
            // 捕获其他异常并返回通用错误信息
            return new ResponseResult(500, "服务器内部错");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 保证在发生异常时进行回滚
    public ResponseResult addPeople(AddPeople addPeople) {
        try {
            activityMapper.addPeople(addPeople);
            return new ResponseResult(200, "添加成功");
        } catch (Exception e) {
            // 返回错误信息
            return new ResponseResult(500, "添加失败，发生错误：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 保证在发生异常时进行回滚
    public ResponseResult deletePeople(AddPeople addPeople) {
        try {
            // 调用mapper根据活动ID和用户ID列表删除人员
            Integer deleteResult = activityMapper.deletePeople(addPeople.getActivityId(), addPeople.getUserId());
            if (addPeople.getUserId().isEmpty()) {
                return new ResponseResult(400, "删除失败，未选择任何用户");
            }
            if (deleteResult == 0){
                return new ResponseResult(400, "删除失败,请检查活动id及用户id");
            }
            return new ResponseResult(200, "删除成功");
        } catch (Exception e) {
            // 返回错误信息
            return new ResponseResult(500, "删除失败，发生错误：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 保证在发生异常时进行回滚
    public ResponseResult bindingIndicators(ActivityIndicators activityIndicators) {
        try {
            Integer bindingResult = activityMapper.bindingIndicators(activityIndicators);
            if (bindingResult == 0){
                return new ResponseResult(400, "绑定失败,请检查活动id及指标id");
            }

            return new ResponseResult(200, "绑定成功");
        }catch (Exception e){
            return new ResponseResult(500, "绑定失败，发生错误：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBinding(Integer activityId, Integer indicatorsId) {
        try {
            Integer deleteResult = activityMapper.deleteBinding(activityId, indicatorsId);
            if (deleteResult == 0){
                return new ResponseResult(400, "删除失败,请检查活动id及指标id");
            }
            return new ResponseResult(200, "删除成功");
        }catch (Exception e){
            return new ResponseResult(500, "删除失败，发生错误：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult score(UserFraction userFraction) {
        UpdateWrapper<UserFraction> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userFraction.getUserId()).eq("activity_id", userFraction.getActivityId()).set("totalScore", userFraction.getTotalScore()).set("others", userFraction.getOthers());
        try {
            int updateResult = userTemporaryMapper.update(updateWrapper);
            if (updateResult == 0){
                return new ResponseResult(400, "更新失败,请检查用户id及活动id");
            }
            return new ResponseResult(200, "更新成功");
        }catch (Exception e){
            return new ResponseResult(500, "更新失败，发生错误：" + e.getMessage());
        }
    }

    @Override
    public ResponseResult list() {
        return new ResponseResult(200, "查询成功", activityMapper.selectList(null));
    }
}
