package com.example.check.service.impl;


import com.example.check.entity.TaskBreakdown;
import com.example.check.entity.pojo.AnnexList;
import com.example.check.mapper.OfficerMapper;
import com.example.check.service.AnnexService;
import com.example.check.service.OfficerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OfficerServiceImpl implements OfficerService {

    @Autowired
    private OfficerMapper officerMapper;
    @Autowired
    private AnnexService annexService;


    @Override
    public String addAnnexs(MultipartFile[] files, Integer breakdownId) {
        annexService.addAnnexs(files, breakdownId, "officer->admin");
        return "success";
    }

    @Override
    public List<AnnexList> getAnnexList(int breakdownId) {
        return annexService.getAnnexList(breakdownId, "admin->officer");
    }

    //    干事端修改子任务状态
    @Override
    public String updateSubTaskStatus(Integer breakdownId, int status) {
        // 判断id是否有效
        if (breakdownId == null || breakdownId <= 0) {
            return "ID无效";
            //  执行更新
        }
        int result = officerMapper.updateSubTaskStatus(breakdownId, status);
        return result > 0 ? "更新成功" : "更新失败";
    }


    //    干事被分配
    @Override
    public List<TaskBreakdown> getMyAssignedTasks(String officeAddress) {

        if (officeAddress == null) {
            return new ArrayList<>();
        }
        return officerMapper.getMyAssignedTasks(officeAddress);


    }

    @Override
    public List<AnnexList> getReplyAnnexList(int breakdownId) {
        return annexService.getAnnexList(breakdownId,"officer->admin");
    }

}
