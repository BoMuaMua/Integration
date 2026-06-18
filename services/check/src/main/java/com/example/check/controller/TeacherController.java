package com.example.check.controller;

import com.example.check.common.Result;
import com.example.check.entity.MainTask;
import com.example.check.entity.pojo.*;
import com.example.check.service.AnnexService;
import com.example.check.service.TeacherService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AnnexService annexService;


    //    上传主任务
    @PostMapping("/add/mainTask")
    public Result postMainTask(@RequestBody MainPostInfo mainPostInfo) {
        return Result.success(teacherService.postMainTask(mainPostInfo));
    }

    //    在列表页面展示主任务
    @GetMapping("/get/mainTask")
    @ResponseStatus(HttpStatus.OK)
    public Result getMainTask(@RequestParam String teacherName, @RequestParam int pageSize, @RequestParam int page) {
        redisTemplate.opsForValue().set("get:mainTask",teacherService.getMainTask(pageSize, page, teacherName), 60 * 60 * 24);
        return Result.success(redisTemplate.opsForValue().get("get:mainTask"));
    }

    //    删除主任务
    @DeleteMapping("/delete/mainTask")
    public Result deleteMainTask(@RequestParam int id) {
        return Result.success(teacherService.deleteMainTask(id));
    }

    //    添加反馈
    @PostMapping("/add/feedback")
    public Result postFeedBack(@RequestBody Feedback feedback) {
        return Result.success(teacherService.postFeedback(feedback));
    }

    //    教师端上传需求（主任务里的一部分）
    @PostMapping("/add/requirements")
    public Result addRequirements(@RequestBody Requirement requirement) {
        teacherService.addRequirements(requirement);
        return Result.success(teacherService.addRequirements(requirement));
    }

    //    教师端删除需求(同时删除需求里带的附件)
    @DeleteMapping("/delete/requirements")
    public Result deleteRequirements(@RequestParam int id) {
        teacherService.deleteRequirements(id);
        return Result.success();
    }

    //    教师端上传附件
    @PostMapping("/add/Annexs")
    public Result addAnnexs(MultipartFile[] file, Integer requirementId) {
        return Result.success(teacherService.addAnnexs(file, requirementId));

    }

//    教师查看需求里附件列表
    @GetMapping("/get/AnnexList")
    public Result getAnnexList(@RequestParam int requirementId) {
        return Result.success(teacherService.getAnnexList(requirementId));
    }

//    教师查看附件详情
//    @GetMapping("/get/AnnexDetail")
//    public Result getAnnexDetail(@RequestParam String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        teacherService.getAnnexDetail(objectName);
//        return Result.success(teacherService.getAnnexDetail(objectName));
//    }

//    教师下载附件
    @GetMapping("/download/Annex")
    public ResponseEntity<byte[]> downloadAnnex(@RequestParam String objectName, HttpServletResponse response) {
        AnnexDownloadNew annex = annexService.downloadAnnex(objectName);
        // 2. 组装响应头
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(annex.getAnnexName())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        // 如果 mimeType 为空，默认使用二进制流
        String contentType = (annex.getMimeType() != null) ? annex.getMimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        // 3. 包装返回体
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .body(annex.getAnnexData());
    }
}





























































