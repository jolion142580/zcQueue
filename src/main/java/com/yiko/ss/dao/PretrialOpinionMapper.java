package com.yiko.ss.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.ss.domain.PretrialOpinion;

import java.util.List;
import java.util.Map;

public interface PretrialOpinionMapper extends MyMapper<PretrialOpinion> {
    List<PretrialOpinion> selectByConditions(Map<String, Object> map);
}