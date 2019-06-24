package com.yiko.ss.dao;

import com.yiko.ss.domain.AffairObject;

import java.util.List;
import java.util.Map;

public interface AffairObjectMapper {
    int deleteByPrimaryKey(String objid);

    int deleteByAffairId(String affairId);

    int insert(AffairObject record);

    int insertSelective(AffairObject record);

    AffairObject selectByPrimaryKey(String objid);

    int updateByPrimaryKeySelective(AffairObject record);

    int updateByPrimaryKey(AffairObject record);

    List<AffairObject> list(Map<String, Object> map);

    int batchRemove(String[] objId);
}