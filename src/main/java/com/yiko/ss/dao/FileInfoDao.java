package com.yiko.ss.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.ss.domain.FileInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface FileInfoDao extends MyMapper<FileInfoDO> {
    List<FileInfoDO> list(Map<String,Object> map);
}