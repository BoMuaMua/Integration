package com.example.check.service;

import com.example.check.entity.Annex;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.check.entity.pojo.AnnexDownloadNew;
import com.example.check.entity.pojo.AnnexList;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author hp
* @description 针对表【annex】的数据库操作Service
* @createDate 2026-04-25 20:09:19
*/
public interface AnnexService extends IService<Annex> {

    String addAnnexs(MultipartFile[] file, Integer requirementId, String type);

    List<AnnexList> getAnnexList(int requirementId, String type);

    // 下载附件
    AnnexDownloadNew downloadAnnex(String objectName);
}
