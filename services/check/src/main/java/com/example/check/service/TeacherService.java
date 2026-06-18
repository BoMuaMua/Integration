package com.example.check.service;

import com.example.check.entity.pojo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {
    String postMainTask(MainPostInfo mainPostInfo);
    List<MainShowInfo> getMainTask(int pageSize,int page,String teacherName);
    String deleteMainTask(int id);
    String postFeedback(Feedback feedback);

    String addAnnexs(MultipartFile[] file, Integer requirementId);

    String addRequirements(Requirement requirement);

    void deleteRequirements(int id);

    List<AnnexList> getAnnexList(int requirementId);
}
