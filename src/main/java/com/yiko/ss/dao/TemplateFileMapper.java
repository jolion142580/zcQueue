package com.yiko.ss.dao;

import com.yiko.ss.domain.TemplateFile;

import java.util.List;
import java.util.Map;

public interface TemplateFileMapper {
    int insert(TemplateFile record);

    int insertSelective(TemplateFile record);

    List<TemplateFile> list(Map<String, Object> map);

    TemplateFile get(Map<String, Object> map);

    int update(TemplateFile templateFile);

    TemplateFile selectByPrimaryKey(String id);

    int remove(String id);

    int batchRemove(String[] ids);
}