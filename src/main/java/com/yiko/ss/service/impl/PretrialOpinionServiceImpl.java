package com.yiko.ss.service.impl;

import com.yiko.common.service.impl.BaseServiceImpl;
import com.yiko.ss.dao.PretrialOpinionMapper;
import com.yiko.ss.domain.PretrialOpinion;
import com.yiko.ss.service.PretrialOpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PretrialOpinionServiceImpl extends BaseServiceImpl<PretrialOpinion> implements PretrialOpinionService {
    @Autowired
    PretrialOpinionMapper pretrialOpinionMapper;

    @Override
    protected void setMapping() {

    }

    @Override
    public List<PretrialOpinion> selectByConditions(Map<String, Object> map) {
        return pretrialOpinionMapper.selectByConditions(map);
    }
}
