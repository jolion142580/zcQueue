package com.yiko.ss.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.common.utils.StringUtils;
import com.yiko.ss.domain.YmsRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface YmsRecordMapper extends MyMapper<YmsRecord> {

    List<YmsRecord> listByNotNull(YmsRecord ymsRecord);

    List<YmsRecord> queryHandlingMattersByDate(@Param("startTime") String startTime, @Param("endTime") String endTime);
}