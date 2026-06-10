package com.example.check.service;

import com.example.check.common.Result;
import com.example.check.entity.MainTask;
import com.example.check.entity.RequirementForm;




public interface AdministratorService {
    Result allMainTask(Integer page, Integer pageSize);

    Result getMainTaskDetail(Integer id);

    Result postSon(RequirementForm requirementForm);

    Result updSonStatus(RequirementForm requirementForm);

    Result updMainStatus(MainTask mainTask);
}