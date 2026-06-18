package com.example.check.controller;

import com.example.check.common.Result;
import com.example.check.entity.MainTask;
import com.example.check.entity.TaskBreakdown;
import com.example.check.entity.pojo.AnnexDownloadNew;
import com.example.check.service.AdministratorService;
import com.example.check.service.AnnexService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private AnnexService annexService;


    //    管理端获取主任务列表
    @GetMapping("/get/allMain")
    @ResponseStatus(HttpStatus.OK)
    public Result allMainTask(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return Result.success(administratorService.allMainTask(page, pageSize));
    }

    //    管理端根据主任务名称查询主任务
    @GetMapping("/get/mainTaskByName")
    @ResponseStatus(HttpStatus.OK)
    public Result getMainTaskByName(@RequestParam String mainTaskName) {
        return Result.success(administratorService.getMainTaskByName(mainTaskName));
    }

    //    管理端根据干事称查询主任务
    @GetMapping("/get/mainTaskByOfficial")
    @ResponseStatus(HttpStatus.OK)
    public Result getMainTaskByOfficial(@RequestParam String Official) {
        List<MainTask> mainTask = administratorService.getMainTaskByOfficial(Official);
        if(mainTask.isEmpty()){
            return Result.success("此干事没有被分配任务");
        }else {
            return Result.success(mainTask);
        }
    }

    //    获取所有干事姓名
    @GetMapping("/get/allUser")
    @ResponseStatus(HttpStatus.OK)
    public Result getAllUserByDepartment(@RequestParam Integer departmentId) {
        return Result.success(administratorService.getUsers(departmentId));
    }

    //    管理端获取主任务需求
    @GetMapping("/get/mainRequirement")
    public Result getMainTaskRequire(@RequestParam Integer mainTaskId) {
        return Result.success(administratorService.getMainTaskRequire(mainTaskId));
    }

    //    管理端修改主任务状态
    @PostMapping("/update/mainStatus")
    public Result updMainStatus(@RequestParam int status, @RequestParam Integer mainTaskId) {
        return Result.success(administratorService.updMainStatus(status,mainTaskId));
    }


    //    管理端发布子任务
    @PostMapping("/post/sonTask")
    public Result postSon(@RequestBody TaskBreakdown taskBreakdown) {
        return Result.success(administratorService.postSon(taskBreakdown));
    }

    //    管理端获取主任务id下所有子任务
    @GetMapping("/get/allSonTask")
    @ResponseStatus(HttpStatus.OK)
    public Result allSonTask(@RequestParam Integer mainTaskId) {
        return Result.success(administratorService.allSonTask(mainTaskId));
    }

    //    删除子任务
    @DeleteMapping("/post/deleteSonTask")
    public Result deleteSonTask(@RequestParam Integer sonTaskId) {
        return Result.success(administratorService.deleteSonTask(sonTaskId));
    }

    //    通过子任务id获取子任务详情
    @GetMapping("/get/SonTaskDetail")
    public Result getSonTaskDetail(@RequestParam Integer sonTaskId) {
        return Result.success(administratorService.getSonTaskDetail(sonTaskId));
    }



    //    管理端修改子任务状态
    @PostMapping("/update/sonStatus")
    public Result updSonStatus(@RequestParam int status, @RequestParam Integer sonTaskId) {
        return Result.success(administratorService.updSonStatus(status,sonTaskId));
    }







    //    管理端上传子桶附件
    @PostMapping("/add/Annexs")
    public Result addAnnexs(MultipartFile[] files, Integer breakdownId) {
        return Result.success(administratorService.addAnnexs(files, breakdownId));
    }

    //    管理查看需求里附件列表
    @GetMapping("/get/MainAnnexList")
    @ResponseStatus(HttpStatus.OK)
    public Result getMainAnnexList(@RequestParam int requirementId) {
        return Result.success(administratorService.getMainAnnexList(requirementId));
    }


    //    管理下载需求附件
    @GetMapping("/download/MainAnnex")
    public ResponseEntity<byte[]> downloadMainAnnex(@RequestParam String objectName, HttpServletResponse  response) {
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


    //    管理查看子任务里附件列表
    @GetMapping("/get/SonAnnexList")
    @ResponseStatus(HttpStatus.OK)
    public Result getSonAnnexList(@RequestParam int breakdownId) {
        return Result.success(administratorService.getSonAnnexList(breakdownId));
    }


    //    管理下载子任务附件
    @GetMapping("/download/SonAnnex")
    public ResponseEntity<byte[]> downloadSonAnnex(@RequestParam String objectName,HttpServletResponse response) {
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

    //    管理查看子任务里附件列表
    @GetMapping("/get/ReplyAnnexList")
    public Result getReplyAnnexList(@RequestParam int breakdownId) {
        return Result.success(administratorService.getReplyAnnexList(breakdownId));
    }


    //    管理下载子回复任务附件
    @GetMapping("/download/ReplyAnnex")
    public ResponseEntity<byte[]> downloadReplyAnnex(@RequestParam String objectName,HttpServletResponse response) {
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

