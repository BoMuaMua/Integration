package com.example.check.service.impl;

import com.example.check.entity.MainTask;
import com.example.check.entity.RequirementForm;
import com.example.check.entity.TaskBreakdown;
import com.example.check.entity.pojo.AnnexList;
import com.example.check.entity.pojo.InfoUser;
import com.example.check.entity.pojo.MainShowInfo;
import com.example.check.mapper.AdministratorMapper;
import com.example.check.service.AdministratorService;
import com.example.check.service.AnnexService;
import com.example.check.utils.RequestUtil;

import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Data
@Service
@Slf4j
public class AdministratorServiceImpl implements AdministratorService {

    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private AnnexService annexService;


    @Autowired
    RequestUtil requestUtil;


    //    主任务信息
//    教师页面获取所有主任务信息（以列表方式渲染）
    @Override
    public List<MainShowInfo> allMainTask(Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        System.out.println(page + "," + pageSize + "," + offset);
        List<MainTask> MainShowInfos = administratorMapper.allMainTask(pageSize, offset);
        return MainShowInfos.stream()
                .map(this::mapToMainShowInfo)
                .collect(Collectors.toList());
    }

    private MainShowInfo mapToMainShowInfo(MainTask mainTask) {
        MainShowInfo MainShowInfo = new MainShowInfo();
        MainShowInfo.setMainTaskID(mainTask.getMainTaskID());
        MainShowInfo.setTag(mainTask.getTag());
        MainShowInfo.setTaskName(mainTask.getTaskName());
        MainShowInfo.setDateLimit(mainTask.getDateLimit());
        MainShowInfo.setTeacher(mainTask.getTeacher());
        return MainShowInfo;
    }

    //  获取所有干事信息
    @Override
    @Cacheable(value = "sonOfficials", key = "#departmentId", unless = "#result == null")
    public List<InfoUser> getUsers(Integer departmentId) {
        List<InfoUser> users = requestUtil.getUserInfoList(Integer.toString(departmentId));
        System.out.println(users);
        List<InfoUser> empolys = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if ("部门员工".equals(users.get(i).getRole())) {
                InfoUser infoUser = new InfoUser();
                infoUser.setRoleId(users.get(i).getRoleId());
                infoUser.setUsername(users.get(i).getUsername());
                infoUser.setDepartment(users.get(i).getDepartment());
                infoUser.setDepartmentId(users.get(i).getDepartmentId());
                infoUser.setId(users.get(i).getId());
                infoUser.setRole(users.get(i).getRole());
                infoUser.setAvatar(users.get(i).getAvatar());
                infoUser.setEmail(users.get(i).getEmail());
                infoUser.setPhone(users.get(i).getPhone());
                infoUser.setCreateTime(users.get(i).getCreateTime());
                infoUser.setStatus(users.get(i).getStatus());
                empolys.add(infoUser);
            }
        }

        return empolys;
    }

    //    根据主任务名获取主任务
    @Override
    public List<MainTask> getMainTaskByName(String mainTaskName) {
        List<MainTask> mainTasks = administratorMapper.getMainTaskByName(mainTaskName);
        return mainTasks;
    }

    //    根据干事名获取主任务
    @Override
    public List<MainTask> getMainTaskByOfficial(String official) {
        Integer mainTaskId = administratorMapper.getMainTaskIdByOfficial(official);
        if (mainTaskId == null) {
            log.warn("负责人 {} 没有关联的主任务ID", official);
            return new ArrayList<>(); // 或者返回 new ArrayList<>()
        }

        // 3. 只有 ID 存在时才去查列表
        return administratorMapper.getMainTaskByOfficial(mainTaskId);
    }

    //    修改主任务状态
    @Override
    public String updMainStatus(int status, Integer mainTaskId) {
        administratorMapper.updaMainStatus(status, mainTaskId);
        return "success";
    }


    //    管理端查询主任务需求
    @Override
    public RequirementForm getMainTaskRequire(int mainTaskId) {
        RequirementForm mainTaskRequest = administratorMapper.getMainTaskRequest(mainTaskId);
        return mainTaskRequest;
    }

    //    发布子任务
    @Override
    public Integer postSon(TaskBreakdown taskBreakdown) {
        taskBreakdown.setTag(1);
        LocalDateTime createTime = LocalDateTime.now();
        administratorMapper.postSonTask(taskBreakdown, createTime);
        Integer breakdownId = administratorMapper.getSonId(taskBreakdown);
        return breakdownId;
    }

    //    按主任务id获取当下所有子任务
    @Override
    public List<TaskBreakdown> allSonTask(Integer MaintaskId) {
        List<TaskBreakdown> taskBreakdowns = administratorMapper.allSonTask(MaintaskId);
        return taskBreakdowns;
    }

    //    按子任务id获取子任务详情
    @Override
    public TaskBreakdown getSonTaskDetail(Integer sonTaskId) {
        TaskBreakdown taskBreakdowns = administratorMapper.getSonTaskDetail(sonTaskId);
        return taskBreakdowns;
    }


    //    修改子任务状态
    @Override
    public String updSonStatus(int status, Integer sonTaskId) {
        administratorMapper.updaSonStatus(status, sonTaskId);
        return "success";
    }

    //    根据id删除子任务
    @Override
    public String deleteSonTask(Integer sonTaskId) {
        administratorMapper.deleteSonTask(sonTaskId);
        return "success";
    }


    //    管理在子桶中上传文件
    @Override
    public String addAnnexs(@RequestParam("files")MultipartFile[] files, Integer breakdownId) {
       annexService.addAnnexs(files, breakdownId,"admin->officer");
        return "上传成功";
    }


    //    管理员在子桶中获取文件列表
    @Override
    public List<AnnexList> getSonAnnexList(int breakdownId) {
        return annexService.getAnnexList(breakdownId, "admin->officer");
    }


    //    管理员在主桶中获取文件列表
    @Override
    public List<AnnexList> getMainAnnexList(int breakdownId) {
        return annexService.getAnnexList(breakdownId, "admin->teacher");
    }


    //    回复附件列表
    @Override
    public List<AnnexList> getReplyAnnexList(int breakdownId) {
        return annexService.getAnnexList(breakdownId, "officer->admin");
    }


}