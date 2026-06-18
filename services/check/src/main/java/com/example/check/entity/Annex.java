package com.example.check.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.OffsetDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import lombok.Data;

/**
 * 
 * @TableName annex
 */
@TableName(value ="annex")
@Data
public class Annex {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long annexId;

    /**
     * 拆分任务的id
     */
    private Integer breakdownId;

    /**
     * 附件名称
     */
    private String annexName;

    /**
     * 附件的区分类型
     */
    private String type;

    /**
     * 附件的MIME类型
     */
    private String mimeType;  // image/jpeg

    /**
     * 附件类型
     */
    private String annexType;

    /**
     * 附件数据
     */
    @Lob
    @Column(columnDefinition = "LONGTEXT") // 确保容量足够
    private String annexData;

    /**
     * 附件大小
     */
    private Long annexSize;

    /**
     * 上传时间
     */
    private OffsetDateTime createTime;
}