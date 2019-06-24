package com.yiko.ss.service;


import com.yiko.common.service.BaseService;
import com.yiko.common.utils.PageUtils;
import com.yiko.ss.domain.FileInfoDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface FileInfoService extends BaseService<FileInfoDO> {

//    PageUtils list(Map<String, Object> map) throws Exception;
    List<FileInfoDO> list(Map<String, Object> map) throws Exception;

}
