package com.example.check.service.impl;

import com.example.check.common.Result;
import com.example.check.entity.MainTask;
import com.example.check.entity.RequirementForm;
import com.example.check.entity.pojo.MainShowDetail;
import com.example.check.entity.pojo.MainShowInfo;
import com.example.check.mapper.AdministratorMapper;

import com.example.check.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements AdministratorService {

    @Autowired
    private AdministratorMapper administratorMapper;


    //    *主视图信息
    @Override
    public Result allMainTask(Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return MainShowInfo(pageSize, offset);
    }


    private MainShowInfo mapToMainShowInfo(MainTask mainTask) {
        MainShowInfo MainShowInfo = new MainShowInfo();
        MainTask MainTask = new MainTask();
        MainShowInfo.setTag(MainTask.getTag());
        MainShowInfo.setTaskName(MainTask.getTaskName());
        MainShowInfo.setDateLimit(MainTask.getDateLimit());
        MainShowInfo.setTeacher(MainTask.getTeacher());

        return MainShowInfo;
    }

    Result MainShowInfo(Integer pageSize, Integer offset) {
        List<MainTask> MainShowInfos = administratorMapper.allMainTask(pageSize, offset);
        return Result.success(MainShowInfos.stream()
                .map(this::mapToMainShowInfo)
                .collect(Collectors.toList()));
    }
//    主视图信息

    //    *主任务细节
    @Override
    public Result getMainTaskDetail(Integer id) {
        return MainShowDetail(id);
    }

    private MainShowDetail mapToMainShowDetail(MainTask mainTask) {
        MainShowDetail MainShowDetail = new MainShowDetail();
        MainTask MainTask = new MainTask();
        MainShowDetail.setMainTaskID(MainTask.getMainTaskID());
        MainShowDetail.setTaskName(MainTask.getTaskName());
        MainShowDetail.setDateLimit(MainTask.getDateLimit());
        MainShowDetail.setTeacher(MainTask.getTeacher());
        MainShowDetail.setDepartment(MainTask.getDepartment());
        MainShowDetail.setPhoneNumber(MainTask.getPhoneNumber());
        MainShowDetail.setOfficeAddress(MainTask.getOfficeAddress());
        return MainShowDetail;
    }

    Result MainShowDetail(Integer id) {
        MainTask MainShowDetails = administratorMapper.selectMainTask(id);
        return Result.success(mapToMainShowDetail(MainShowDetails));
    }
//    主任务细节

    //    发布子任务
    @Override
    public Result postSon(RequirementForm requirementForm) {
        administratorMapper.postSonTask(requirementForm);
        return Result.success("success");
    }

    @Override
    public Result updMainStatus(MainTask mainTask) {
        administratorMapper.updaMainStatus(mainTask);
        return Result.success("success");
    }

    @Override
    public Result updSonStatus(RequirementForm requirementForm) {
        administratorMapper.updaSonStatus(requirementForm);
        return Result.success("success");
    }


}
