package com.example.check.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.check.entity.Annex;
import com.example.check.entity.pojo.AnnexDownloadNew;
import com.example.check.entity.pojo.AnnexDownloadNewDO;
import com.example.check.entity.pojo.AnnexList;
import com.example.check.service.AnnexService;
import com.example.check.mapper.AnnexMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;

/**
* @author hp
* @description 针对表【annex】的数据库操作Service实现
* @createDate 2026-04-25 20:09:19
*/
@Service
@Slf4j
public class AnnexServiceImpl extends ServiceImpl<AnnexMapper, Annex>
    implements AnnexService{

    @Autowired
    private AnnexMapper annexMapper;


    // 添加附件
    @Override
    public String addAnnexs(MultipartFile[] file, Integer requirementId, String type) {
        if (file == null || file.length == 0) {
            // 如果没有文件，可以直接返回或抛出异常
            return ("未选择任何文件。");
        }

        try {
            for (MultipartFile file_entity : file) {
                if (file_entity.isEmpty()) {
                    log.info("警告: 发现空文件"+file_entity.getOriginalFilename()+"，已跳过。");
                    continue; // 跳过空文件
                }
                if (!file_entity.isEmpty()) {
                    // 获取字节数组
                    byte[] bytes = file_entity.getBytes();

                    // 1. 获取原始文件名
                    String originalFileName = file_entity.getOriginalFilename();

                    // 2. 转换为 Base64 字符串
                    String base64String = Base64.getEncoder().encodeToString(bytes);

                    // 3. 获取文件后缀名（如 "jpg"）
                    String extension = "";
                    if (originalFileName != null && originalFileName.contains(".")) {
                        extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                    }
                    // 4. 获取 MIME 类型（如 "image/jpeg" 或 "application/pdf"）
                    String contentType = file_entity.getContentType();

                    // 5. 获取不带后缀的文件名（如 "photo"）
                    String fileNameWithoutExt = "";
                    if (originalFileName != null && originalFileName.contains(".")) {
                        fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf("."));
                    }
                    // 添加UUID确保文件名唯一性
                    fileNameWithoutExt = fileNameWithoutExt + "_" + java.util.UUID.randomUUID().toString().substring(0, 8);

                    // 3. 保存到数据库
                    Annex  annex = new Annex();
                    annex.setBreakdownId(requirementId);
                    annex.setAnnexName(fileNameWithoutExt);
                    annex.setType(type);
                    annex.setMimeType(contentType);
                    annex.setAnnexType(extension);
                    annex.setAnnexData(base64String);
                    annex.setAnnexSize(file_entity.getSize());
                    annex.setCreateTime(OffsetDateTime.now());
                    annexMapper.insert(annex);
                }
            }
            return "success";
        } catch (Exception e) {
            log.error("文件上传失败: " + e.getMessage());
            // 返回错误信息
            return "文件上传失败: " + e.getMessage();
        }
    }

    // 获取附件列表
    @Override
    public List<AnnexList> getAnnexList(int requirementId, String type) {
        return annexMapper.getAnnexList(requirementId,type);
    }

    // 下载附件
    @Override
    public AnnexDownloadNew downloadAnnex(String objectName) {
        // 从数据库提取附件数据
        AnnexDownloadNewDO annex = annexMapper.getData(objectName);
        if (annex == null) {
            throw new RuntimeException("附件不存在: " + objectName);
        }

        // 将 Base64 解码为字节数组
        byte[] data = Base64.getDecoder().decode(annex.getAnnexData());
        String mimeType = (annex.getMimeType() != null) ? annex.getMimeType() : "application/octet-stream";
        return AnnexDownloadNew.builder()
                .annexName(annex.getAnnexName() + "." + annex.getAnnexType())
                .mimeType(mimeType)
                .annexData(data)
                .build();
    }

}




