package com.example.check.controller;

import com.example.check.common.Result;
import com.example.check.entity.MainTask;
import com.example.check.entity.RequirementForm;
import com.example.check.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @GetMapping("/maintask/getAllmain")
    Result allMainTask(@RequestParam(defaultValue = "1") Integer p, @RequestParam(defaultValue = "10") Integer ps) {
        return administratorService.allMainTask(p, ps);
    }

    @PostMapping("/maintask/updastatus")
    Result updMainStatus(@RequestBody MainTask mainTask) {
        return administratorService.updMainStatus(mainTask);
    }

    @PostMapping("/maintask/postson")
    Result postSon(@RequestBody RequirementForm requirementForm) {
        return administratorService.postSon(requirementForm);
    }

}
