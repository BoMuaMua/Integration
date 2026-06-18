package com.example.check.mapper;

import com.example.check.entity.TaskBreakdown;
import com.example.check.entity.pojo.AnnexList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OfficerMapper {

    // 更新任务状态
    Integer updateSubTaskStatus(Integer breakdownId,  int status);

    List<TaskBreakdown> getMyAssignedTasks(String officeAddress);
}
