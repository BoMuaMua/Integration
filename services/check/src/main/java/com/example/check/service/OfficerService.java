package com.example.check.service;


import com.example.check.common.Result;
import com.example.check.entity.TaskBreakdown;
import com.example.check.entity.pojo.AnnexList;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OfficerService {

    String addAnnexs(MultipartFile[] files, Integer breakdownId);

    List<AnnexList> getAnnexList(int breakdownId);

    String updateSubTaskStatus(Integer breakdownId, int status);

    List<TaskBreakdown> getMyAssignedTasks(String officeAddress);

    List<AnnexList> getReplyAnnexList(int breakdownId);
}
