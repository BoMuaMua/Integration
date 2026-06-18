package com.example.check.mapper;

import com.example.check.entity.Annex;
import com.example.check.entity.MainTask;
import com.example.check.entity.pojo.AnnexList;
import com.example.check.entity.pojo.Feedback;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TeacherMapper {
    List<MainTask> showMainTask(int pageSize,int offset,String teacherName);
    void postMainTask(MainTask mainTask, LocalDateTime createTime);
    void deleteMainTask(int id);
    void postFeedback(Feedback feedback);



    void addRequirements(Integer mainTaskId, String requirementDetail, String otherInfo, Integer tag, LocalDateTime createTime);

    void deleteRequirementsRequirement(int id);
    void deleteRequirementsAnnex(int id);

    List<AnnexList> getObjectsName(int requirementId);

    // 教师上传附件
    void addAnnex(Annex annex);
}
