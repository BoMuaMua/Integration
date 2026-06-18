package com.example.check.service;

import com.example.check.common.Result;
import com.example.check.entity.MainTask;
import com.example.check.entity.RequirementForm;
import com.example.check.entity.TaskBreakdown;
import com.example.check.entity.pojo.AnnexList;
import com.example.check.entity.pojo.MainShowDetail;
import com.example.check.entity.pojo.MainShowInfo;
import com.example.check.entity.pojo.InfoUser;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AdministratorService {
    List<MainShowInfo> allMainTask(Integer page, Integer pageSize);

    List<InfoUser> getUsers(Integer DepartmentId);

    RequirementForm getMainTaskRequire(int mainTaskId);

    List<MainTask> getMainTaskByName(String mainTaskName);

    List<MainTask> getMainTaskByOfficial (String official);

    String updMainStatus(int status,Integer id);

    Integer postSon(TaskBreakdown taskBreakdown);

    List<TaskBreakdown> allSonTask(Integer MaintaskId);

    TaskBreakdown getSonTaskDetail(Integer SonTaskId);

    String updSonStatus(int status,Integer id);

    String deleteSonTask(Integer SonTaskId);

    String addAnnexs(MultipartFile[] files,Integer breakdownId);

    List<AnnexList> getSonAnnexList(int requirementId);

    List<AnnexList> getMainAnnexList(int requirementId);

    List<AnnexList> getReplyAnnexList(int breakdownId);
}