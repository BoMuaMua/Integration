package com.example.signin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.signin.entity.Activity;
import com.example.signin.entity.ActivityList;
import com.example.signin.entity.SignDetail;
import com.example.signin.entity.upActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityMapper extends BaseMapper<Activity> {

    Integer selectByActivityName(String activityName);

    Integer selectTypeByActivityName(String activityName);

    Integer insertActivity(Activity activity, String userIdString, String departmentIdString, LocalDateTime now);

    LocalDateTime getCreateTime(String activityName, int activityId);

    String selectDepts(String activityName, int activityId);

    String selectDeptNameById(String id);

    String getDeptNameByDeptId(int deptId);

    List<ActivityList> selectAllActivity();

    void updateActivity(upActivity activity);

    String getActivityData(String activityName);

    void delateActivity1(String activityName);

    boolean selectByActivityName1(String activityName);

    String getDescription(String activityName, int activityId);

    String getActivityNameByActivityId(int id);

    LocalDateTime selectCreateTime(String activityName, Integer activityId);

    Activity selectActivityByName(String activityName);

    List<ActivityList> selectActivityByUserId(Long id);

    List<ActivityList> selectAllActivities();

    SignDetail selectActivityDetailByName(String activityName);

    SignDetail selectActivitiesByName(String activityName);
}
