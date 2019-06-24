package com.yiko.ss.dao;

import com.yiko.ss.domain.AffairGuideDO;

import java.util.List;
import java.util.Map;

public interface AffairGuideDao {
    /*i

    int insert(AffairGuideDO record);

    int insertSelective(AffairGuideDO record);


    int updateByPrimaryKeySelective(AffairGuideDO record);

    int updateByPrimaryKey(AffairGuideDO record);*/

    AffairGuideDO selectByPrimaryKey(String guideId);


    int deleteByAffairId(String affairId);

    int save(AffairGuideDO record);

    int insertSelective(AffairGuideDO affairGuideDO);

    int remove(String guideId);

    int batchRemove(String[] guideId);

    int update(AffairGuideDO record);

    AffairGuideDO getByAffairId(String affairId);

    List<AffairGuideDO> list(Map<String,Object> map);
}