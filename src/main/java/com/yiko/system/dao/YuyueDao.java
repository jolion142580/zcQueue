package com.yiko.system.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.system.domain.YuyueDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YuyueDao extends MyMapper<YuyueDO> {

    //List<YuyueDO> list(Map<String, Object> map);

    //int count(Map<String, Object> map);
}