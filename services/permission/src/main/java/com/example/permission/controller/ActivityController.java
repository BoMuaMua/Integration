package com.example.permission.controller;

import com.example.permission.entity.*;
import com.example.permission.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/get")
    public ResponseResult list(){
        return activityService.list();
    }

    @PostMapping("/add")
    public ResponseResult addActivity(@RequestBody Participants participants){
        return activityService.addActivity(participants);
    }
    @DeleteMapping("/delete")
    public ResponseResult deleteActivity(@RequestParam Integer activityId){
        return activityService.deleteActivity(activityId);
    }

    @PutMapping("/update")
    public ResponseResult updateActivity(@RequestBody Participants participants){
        return activityService.updateActivity(participants);
    }

    @PostMapping("/addPeople")
    public ResponseResult addPeople(@RequestBody AddPeople addPeople){
        return activityService.addPeople(addPeople);
    }

    @DeleteMapping("/deletePeople")
    public ResponseResult deletePeople(@RequestBody AddPeople addPeople){
        return activityService.deletePeople(addPeople);
    }

    @PostMapping("/bindingIndicators")
    public ResponseResult bindingIndicators(@RequestBody ActivityIndicators activityIndicators){
        return activityService.bindingIndicators(activityIndicators);
    }

    @DeleteMapping("/deleteBinding")
    public ResponseResult deleteBinding(@RequestParam Integer activityId, @RequestParam Integer indicatorsId){
        return activityService.deleteBinding(activityId, indicatorsId);
    }

    @PutMapping("/score")
    public ResponseResult score(@RequestBody UserFraction userFraction){
        return activityService.score(userFraction);
    }

}
