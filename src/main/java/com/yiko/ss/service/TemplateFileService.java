package com.yiko.ss.service;

import com.yiko.common.domain.Tree;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.domain.TemplateFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TemplateFileService {

    Tree<BaseDicDO> getTree();

    List<TemplateFile> list(Map<String, Object> map);

    TemplateFile get(Map<String, Object> map);

    int save(TemplateFile templateFile);

    R saveOrUpdate(String templateFile, MultipartFile file);

    void remove(String id);

    int batchRemove(String[] ids);



}
