package com.example.permission.controller;

import com.example.permission.entity.ResponseResult;
import com.example.permission.entity.Year;
import com.example.permission.mapper.YearMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/year")
public class YearController {
    @Autowired
    private YearMapper yearMapper;

    @GetMapping("/get")
    public ResponseResult get() {
        List<Year> years = yearMapper.selectList(null);
        List<String> yearString = new ArrayList<>();
        for (Year year : years) {
            yearString.add(year.getYear()+"学年");
        }
        return new ResponseResult(200, "查询成功", yearString);
    }

    @PostMapping("/add")
    public ResponseResult add() {
        List<Year> years = yearMapper.selectList(null);
        int maxYear = years.stream()
                .mapToInt(Year::getYear) // 转为 IntStream
                .max()
                .orElse(0);

        // 获取当前年份
        int currentYear = java.time.Year.now().getValue();

        // 计算差值（绝对值）
        int diff = Math.abs(maxYear - currentYear);
        if (diff <= 1){
            Year year = new Year();
            year.setYear(currentYear);
            yearMapper.insert(year);
            return new ResponseResult(200, "添加成功");
        }else{
            return new ResponseResult(400, "不要再建了，建那么多年你选的过来吗");
        }
    }
}
