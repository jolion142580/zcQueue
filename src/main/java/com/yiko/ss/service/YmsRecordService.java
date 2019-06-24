package com.yiko.ss.service;

import com.yiko.common.service.BaseService;
import com.yiko.common.utils.StringUtils;
import com.yiko.ss.domain.YmsRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface YmsRecordService extends BaseService<YmsRecord> {
    List<YmsRecord> listByNotNull(YmsRecord ymsRecord);

    /**
     * 所有已完结事项
     *
     * @return
     */
    List<YmsRecord> handlingMatters();

    /**
     * 根据日期完结事项
     *
     * @return
     */
    List<YmsRecord> queryHandlingMattersByDate(@Param("startTime") String startTime, @Param("endTime") String endTime);

}
