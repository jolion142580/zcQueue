package com.yiko.ss.service;

import com.yiko.common.service.BaseService;
import com.yiko.ss.domain.PretrialOpinion;

import java.util.List;
import java.util.Map;

public interface PretrialOpinionService extends BaseService<PretrialOpinion> {
    List<PretrialOpinion> selectByConditions(Map<String, Object> map);


}
