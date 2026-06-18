package com.example.check.service.impl;


import com.example.check.entity.Annex;
import com.example.check.entity.MainTask;
import com.example.check.entity.pojo.*;
import com.example.check.service.AnnexService;
import com.example.check.service.TeacherService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.check.mapper.TeacherMapper;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private AnnexService annexService;


    //    发布主任务
    @Override
    public String postMainTask(MainPostInfo mainPostInfo) {

        if (Objects.isNull(mainPostInfo.getOfficeAddress()) || mainPostInfo.getOfficeAddress().isEmpty() || Objects.isNull(mainPostInfo.getTaskName()) || mainPostInfo.getTaskName().isEmpty() || Objects.isNull(mainPostInfo.getTeacher()) || mainPostInfo.getTeacher().isEmpty() || Objects.isNull(mainPostInfo.getDepartment()) || mainPostInfo.getDepartment().isEmpty() || Objects.isNull(mainPostInfo.getPhoneNumber()) || mainPostInfo.getPhoneNumber().isEmpty()) {

            return "error: Required field is empty or null.";
        } else {
            LocalDateTime createTime = LocalDateTime.now();
            teacherMapper.postMainTask(maptoMainTask(mainPostInfo), createTime);
            return "success";
        }
    }


    public MainTask maptoMainTask(MainPostInfo mainPostInfo) {
        MainTask mainTask = new MainTask();
//        mainTask.setMainTaskID(mainPostInfo.getMainTaskID());
        mainTask.setTaskName(mainPostInfo.getTaskName());
        mainTask.setTeacher(mainPostInfo.getTeacher());
        mainTask.setDepartment(mainPostInfo.getDepartment());
        mainTask.setPhoneNumber(mainPostInfo.getPhoneNumber());
        mainTask.setOfficeAddress(mainPostInfo.getOfficeAddress());
        mainTask.setTag(1);
        LocalDate currentDate = LocalDate.now();
        java.util.Date utilDate = java.util.Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        mainTask.setDateLimit(utilDate);
        return mainTask;
    }


    //    根据老师获取主任务
    @Override
    public List<MainShowInfo> getMainTask(int pageSize, int page, String teacherName) {
        return MainShowInfo(pageSize, page, teacherName);
    }
//    *是否要经行分页获取————————————————————————————————————————

    public List<MainShowInfo> MainShowInfo(int pageSize, int page, String teacherName) {
        int offset = (page - 1) * pageSize;
        System.out.println(page + "," + pageSize + "," + offset);
        List<MainTask> mainShowInfos = teacherMapper.showMainTask(pageSize, offset, teacherName);
        return mainShowInfos.stream()
                .map(this::mapToMainShowInfo)
                .collect(Collectors.toList());
    }

    // 包装行为
    private MainShowInfo mapToMainShowInfo(MainTask mainTask) {
        MainShowInfo mainShowInfo = new MainShowInfo();
        mainShowInfo.setTeacher(mainTask.getTeacher());
        mainShowInfo.setTaskName(mainTask.getTaskName());
        mainShowInfo.setDateLimit(mainTask.getDateLimit());
        mainShowInfo.setMainTaskID(mainTask.getMainTaskID());
        mainShowInfo.setTag(mainTask.getTag());

        return mainShowInfo;
    }


    //    删除主任务
    @Override
    @Transactional
    public String deleteMainTask(int id) {
        teacherMapper.deleteMainTask(id);
        return "success";
    }


    //    评价主任务
    @Override
    public String postFeedback(Feedback feedback) {
        teacherMapper.postFeedback(feedback);
        return "success";
    }


    @Override
    public String addRequirements(Requirement requirement) {
        LocalDateTime createTime = LocalDateTime.now();
        Integer mainTaskId = requirement.getMainTaskId();
        String requirementDetail = requirement.getRequirementDetail();
        String otherInfo = requirement.getOtherInfo();
        Integer tag = requirement.getTag();
        teacherMapper.addRequirements(mainTaskId, requirementDetail, otherInfo, tag, createTime);
        return null;
    }

    @Override
    public void deleteRequirements(int id) {
        try {
            teacherMapper.deleteRequirementsRequirement(id);
            teacherMapper.deleteRequirementsAnnex(id);

        } catch (Exception dbException) {
            System.err.println("数据库删除操作失败！");
            throw new RuntimeException("数据库事务失败", dbException);
        }
        teacherMapper.deleteRequirementsRequirement(id);
        teacherMapper.deleteRequirementsAnnex(id);
    }


    //    添加附件
    @Override
    public String addAnnexs(MultipartFile[] file, Integer requirementId) {
        annexService.addAnnexs(file, requirementId, "teacher->admin");
        return "success";

    }

    @Override
    public List<AnnexList> getAnnexList(int requirementId) {
        return annexService.getAnnexList(requirementId, "teacher->admin");
    }

}
