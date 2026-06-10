package com.example.permission.controller;

import com.example.permission.entity.IndicatorsMessage;
import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.deleteIndicators;
import com.example.permission.service.IndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/indicators")
public class IndicatorsController {
    @Autowired
    private IndicatorsService indicatorsService;

    @GetMapping("/get")
    public ResponseResult get(){
        return indicatorsService.get();
    }

    @PostMapping("/add")
    public ResponseResult add(@RequestBody IndicatorsMessage indicatorsMessage){
            return indicatorsService.add(indicatorsMessage);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody IndicatorsMessage indicatorsMessage){
        return indicatorsService.update(indicatorsMessage);
    }

    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestBody deleteIndicators deleteIndicators){
        return indicatorsService.delete(deleteIndicators);
    }

    @GetMapping("/getIndicatorsByActivityId")
    public ResponseResult getIndicatorsByActivityId(@RequestParam Integer activityId) {
        return indicatorsService.getIndicatorsByActivityId(activityId);
    }

    @GetMapping("/getUserIndicators")
    public ResponseResult getUserIndicators(@RequestParam(required = false) Integer year) {
        return indicatorsService.getUserIndicators(year);
    }

    @GetMapping("/getUserIndicatorsByActivityId")
    public ResponseResult getUserIndicatorsByActivityId(@RequestParam Integer activityId) {
        return indicatorsService.getUserIndicatorsByActivityId(activityId);
    }
}
