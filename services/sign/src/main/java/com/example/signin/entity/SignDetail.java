package com.example.signin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignDetail {
    private LocalDateTime time; //发起签到时间
    private  LocalDateTime endTime; //活动结束时间
    private String peopleNumber;  //人数比
    private List<String> departmentName; //参与部门
    private String typeName; //活动类型
    private String activityName; //活动名称
    private String description; //活动描述
    private List<SignUserDetail> signData; //签到人员列表
}
