package com.example.check.mapper;

import com.example.check.entity.MainTask;
import com.example.check.entity.RequirementForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdministratorMapper {
    List<MainTask> allMainTask(Integer pageSize, Integer offset);
    MainTask selectMainTask(Integer id);
    MainTask updaMainStatus(MainTask mainTask);
    RequirementForm postSonTask(RequirementForm requirementForm);
    RequirementForm updaSonStatus(RequirementForm requirementForm);
}
