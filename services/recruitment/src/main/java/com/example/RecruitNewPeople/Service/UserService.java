package com.example.RecruitNewPeople.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.example.RecruitNewPeople.entity.pojo.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    PageBean allInfo(Integer page, Integer pageSize);
    JSONObject departmentData();

    JSONArray InfoByDepartment(String departmentId);

    @Transactional(rollbackFor = Exception.class)
    boolean register(@RequestBody JSONObject json);


    JSONObject getUserByIp(String id);

    void updateUserInfo(Users updateUser, ArrayList updateArrayList);

    void deleteUser(String id);

    JSONArray InfoByPersonal(String key);

    boolean OssPut(MultipartFile image, String id);


    void deleteVolunteer(String id);

    void deleteImage(String id);

    void deleteStatus(String id);

    boolean rememberInComment(Comment comment);

    List <Comment> getComment(String id);

    List<WriteExcel> getExcelData();

    List<throughMembers> throughMembers(String departmentId);
}
