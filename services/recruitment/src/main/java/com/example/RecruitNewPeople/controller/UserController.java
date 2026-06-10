package com.example.RecruitNewPeople.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;

import com.example.RecruitNewPeople.Listener.ExcelDataListener;
import com.example.RecruitNewPeople.Service.UserService;
import com.example.RecruitNewPeople.entity.pojo.*;
import com.example.RecruitNewPeople.mapper.DepartmentMapper;
import com.example.RecruitNewPeople.mapper.ListenerMapper;
import com.example.RecruitNewPeople.mapper.UserMapper;
import com.example.RecruitNewPeople.utils.OssUtil;
import com.example.RecruitNewPeople.utils.ResultUtil;
import com.example.RecruitNewPeople.utils.uuidUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OssUtil ossUtil;
    @Autowired
    UserService user;
    @Autowired
    ListenerMapper listenerMapper;

    @GetMapping("/user/info/all")
    ResultUtil allinfo(@RequestParam(defaultValue  = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize) {

        return ResultUtil.success(user.allInfo(page,pageSize));
    }

    @PostMapping(value = "/putPhoto", consumes = "multipart/form-data")
    public synchronized ResultUtil descriptPhoto(@RequestParam("file") MultipartFile image, @RequestParam String id) throws IOException {
        String fileName = image.getOriginalFilename();
        System.out.println(fileName);
        if (!(fileName != null && (fileName.toLowerCase().endsWith(".jpg") ||
                fileName.toLowerCase().endsWith(".jpeg") ||
                fileName.toLowerCase().endsWith(".png") ||
                fileName.toLowerCase().endsWith(".gif") ||
                fileName.toLowerCase().endsWith(".bmp")))) {
            return new ResultUtil(400, "只能上传图片", null);
        }
        if (id.length() != 12) {
            return new ResultUtil(403, "id不合法", null);
        }
        if (user.OssPut(image, id)) {
            return new ResultUtil(200, "上传成功", null);
        }
        return new ResultUtil(400, "上传失败", null);
    }

    @GetMapping("/user/departmentData")
    public ResultUtil depatementData() {
        return ResultUtil.success(user.departmentData());
    }

    @GetMapping("/user/info/department")
    public ResultUtil infoByDepartmeent(@RequestParam String departmentId) {
        if (!(departmentMapper.departmentExist(departmentId)))
            return new ResultUtil(400, "部门不存在", null);
        return ResultUtil.success(user.InfoByDepartment(departmentId));
    }

    @GetMapping("/user/info/person")
    public ResultUtil InfoByPerson(@RequestParam String key) {
        return ResultUtil.success(user.InfoByPersonal(key));
    }

    @PostMapping("user/register")
    ResultUtil register(HttpServletRequest request, @RequestBody JSONObject json) {
        String ip = "test";
//                request.getHeader("X-Real-IP");

        String id = json.getString("id");
        userMapper.postIp(ip, id);
        if (user.register(json)) {
            return ResultUtil.success();
        }
        return ResultUtil.error();
    }


    @PutMapping("/user/updateImage")
    public ResultUtil update(HttpServletRequest request, @RequestBody JSONObject json) {
        String id = json.getString("id");

        if (!departmentMapper.userExist(id)) {
            return new ResultUtil(403, "你还未报名，请检查学号", null);
        }
        Image updateImage = userMapper.getImageByid(id);
        if (updateImage != null) {
            if (json.containsKey("url")) {
                updateImage.setUrl(json.getString("url"));
            }
            userMapper.updateImage(updateImage);
            return new ResultUtil(200, "用户信息已更新", updateImage);
        } else {
            return new ResultUtil(404, "用户不存在", null);
        }
    }
    @GetMapping("/user/ip")
    ResultUtil getUserByIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String id = userMapper.ipLookups(ip);
        if (id != null) {
            return new ResultUtil(200, "查询成功", user.getUserByIp(id));
        } else {
            return new ResultUtil(404, "用户不存在", null);
        }
    }

    @DeleteMapping("/user/delete")
    ResultUtil deleteUser(@RequestParam String id) {
        if (!departmentMapper.userExist(id)) return new ResultUtil(400, "没有此用户", null);
        user.deleteUser(id);
        user.deleteVolunteer(id);
        user.deleteImage(id);
        user.deleteStatus(id);
        return ResultUtil.success();
    }

    @GetMapping("download/excel")
    public void downloadExcal(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WriteExcel.class).sheet("模板").doWrite(user.getExcelData());
    }

    /**
     * 获取已通过成员的信息
     */
    @GetMapping("/excel/throughMembers")
    public void throughMembers(HttpServletResponse response,@RequestParam String departmentId) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + "passUser.xlsx");
        EasyExcel.write(response.getOutputStream(), throughMembers.class).sheet("模板").doWrite(user.throughMembers(departmentId));
    }

    /**
     * 写入已面试部门的评价
    * */
    @PostMapping("/remember/comment")
    ResultUtil rememberInComment(@RequestBody Comment comment){
        if (user.rememberInComment(comment)) {
            return ResultUtil.success();
        }
        return ResultUtil.error();
    }
    @GetMapping("/comment")
    ResultUtil commentById(@RequestParam String id){
       return ResultUtil.success(user.getComment(id)) ;
    }


    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file != null) {
            EasyExcel.read(file.getInputStream(), ReadExcel.class, new ExcelDataListener(listenerMapper)).sheet().doRead();
            return "success";
        } else {
            return "file 为 null";
        }
    }
}


