package com.yiko.ss.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.ss.domain.YmsFile;
import com.yiko.ss.domain.YmsRecord;

import java.util.List;

public interface YmsFileMapper extends MyMapper<YmsFile> {
    List<YmsFile> listByNotNull(YmsFile ymsFile);

}