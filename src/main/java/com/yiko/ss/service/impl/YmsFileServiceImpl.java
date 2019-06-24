package com.yiko.ss.service.impl;

import com.yiko.common.service.impl.BaseServiceImpl;
import com.yiko.ss.dao.YmsFileMapper;
import com.yiko.ss.domain.YmsFile;
import com.yiko.ss.domain.YmsRecord;
import com.yiko.ss.service.YmsFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YmsFileServiceImpl extends BaseServiceImpl<YmsFile> implements YmsFileService {
    @Autowired
    YmsFileMapper ymsFileMapper;

    @Override
    protected void setMapping() {

    }

    @Override
    public List<YmsFile> listByNotNull(YmsFile ymsFile) {
        return ymsFileMapper.listByNotNull(ymsFile);
    }
}
