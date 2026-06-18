package com.example.check.controller;

import com.example.check.common.Result;

import com.example.check.entity.pojo.AnnexDownloadNew;
import com.example.check.service.AnnexService;
import com.example.check.service.OfficerService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/officer")
public class OfficerController {

    @Autowired
    private OfficerService officerService;

    @Autowired
    private AnnexService annexService;

    //    干事端上传附件
    @PostMapping("/add/Annexs")
    public Result addAnnexs(MultipartFile[] files, Integer breakdownId) {
        return Result.success(officerService.addAnnexs(files, breakdownId));
    }

    //    干事查看需求里附件列表
    @GetMapping("/get/AnnexList")
    public Result getAnnexList(@RequestParam int breakdownId) {
        return Result.success(officerService.getAnnexList(breakdownId));
    }

//    //    干事查看子任务附件详情
//    @GetMapping("/get/AnnexDetail")
//    public Result getAnnexDetail(@RequestParam String objectName){
//        return Result.success(officerService.getAnnexDetail(objectName));
//    }

    //    干事下载子任务附件
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


    @PostMapping("/post/sonStatus")
    public Result updateSubTaskStatus(@RequestParam Integer breakdownId, @RequestParam int status) {
        return Result.success(officerService.updateSubTaskStatus(breakdownId, status));
    }

    @GetMapping("/get/myTasks")
    public Result getMyAssignedTasks(@RequestParam String officeAddress) {
        return Result.success(officerService.getMyAssignedTasks(officeAddress));
    }

    //    干事查看自己上传的附件列表
    @GetMapping("/get/ReplyAnnexList")
    public Result getReplyAnnexList(@RequestParam int breakdownId) {
        officerService.getAnnexList(breakdownId);
        return Result.success(officerService.getReplyAnnexList(breakdownId));
    }


    //    干事下载子任务回复附件
    @GetMapping("/download/ReplyAnnex")
    public ResponseEntity<byte[]> downloadReplyAnnex(@RequestParam String objectName, HttpServletResponse response) {
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
