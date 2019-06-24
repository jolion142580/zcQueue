package com.yiko.ss.dao;

import com.yiko.ss.domain.AffairsDO;

import java.util.List;
import java.util.Map;


public interface AffairsDao {

    int save(AffairsDO record);

    int remove(String affairId);

    int batchRemove(String[] affairId);

    int update(AffairsDO record);

    AffairsDO get(String affairId);

    List<AffairsDO> list(Map<String, Object> map);

//    AffairsDO queryListCname(Map<String, Object> map);

    List<AffairsDO> listByCname(Map<String, Object> map);

    int updateByTemplateId(String templateId);

    int insertSelective(AffairsDO affairsDO);

    int updateByTemplateIds(String[] templateIds);

}