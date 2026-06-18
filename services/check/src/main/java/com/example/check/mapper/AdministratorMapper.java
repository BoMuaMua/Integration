package com.example.check.mapper;

import com.example.check.entity.MainTask;
import com.example.check.entity.RequirementForm;
import com.example.check.entity.TaskBreakdown;
import com.example.check.entity.pojo.AnnexList;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AdministratorMapper {
    List<MainTask> allMainTask(Integer pageSize, Integer offset);

    RequirementForm getMainTaskRequest(int mainTaskId);

    void updaMainStatus(int status,Integer  mainTaskId);

    List<MainTask> getMainTaskByName(String mainTaskName);

    Integer getMainTaskIdByOfficial(String Official);

    List<MainTask> getMainTaskByOfficial(int mainTaskId);

    void postSonTask(TaskBreakdown requirementForm, LocalDateTime createTime);

    void updaSonStatus(int status,Integer  sonTaskId);

    List<TaskBreakdown> allSonTask(Integer mainTaskId);

    TaskBreakdown getSonTaskDetail(Integer sonTaskId);

    void deleteSonTask(Integer sonTaskId);

    Integer getSonId(TaskBreakdown taskBreakdown);
}
