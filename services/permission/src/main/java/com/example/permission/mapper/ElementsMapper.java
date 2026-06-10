package com.example.permission.mapper;

import com.example.permission.entity.Elements;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ElementsMapper extends BaseMapper<Elements> {
    void insert(List<Elements> elements);

    void updateAndInsert(List<Elements> elements);

    void deleteByIndicatorsId(Integer indicatorsId);
    void delete(Integer elementsId);
}
