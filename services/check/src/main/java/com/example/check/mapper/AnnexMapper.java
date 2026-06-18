package com.example.check.mapper;

import com.example.check.entity.Annex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.check.entity.pojo.AnnexDownloadNewDO;
import com.example.check.entity.pojo.AnnexList;

import java.util.List;

/**
* @author hp
* @description 针对表【annex】的数据库操作Mapper
* @createDate 2026-04-25 20:09:19
* @Entity com.example.check.entity.Annex
*/
public interface AnnexMapper extends BaseMapper<Annex> {

    List<AnnexList> getAnnexList(int requirementId, String type);

    AnnexDownloadNewDO getData(String objectName);
}




