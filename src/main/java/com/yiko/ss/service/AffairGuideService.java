package com.yiko.ss.service;

import com.yiko.common.utils.PageUtils;
import com.yiko.ss.domain.AffairGuideDO;

import java.util.List;
import java.util.Map;

public interface AffairGuideService {
    PageUtils queryList(Map<String,Object> map);

    void save(AffairGuideDO affairGuideDO);

    AffairGuideDO getAffairGuideByAffairId(String affairId);

    void insertSelective(AffairGuideDO affairGuideDO);

    int update(AffairGuideDO affairGuideDO);

    int remove(String guideId);

    int batchRemove(String[]guideId);

    //同步办事指南
    void pullAffairGuide();
}
