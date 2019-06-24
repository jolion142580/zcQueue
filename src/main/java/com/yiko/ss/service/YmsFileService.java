package com.yiko.ss.service;

import com.yiko.common.service.BaseService;
import com.yiko.ss.domain.YmsFile;
import com.yiko.ss.domain.YmsRecord;

import java.util.List;

public interface YmsFileService extends BaseService<YmsFile> {
    List<YmsFile> listByNotNull(YmsFile ymsFile);

}
