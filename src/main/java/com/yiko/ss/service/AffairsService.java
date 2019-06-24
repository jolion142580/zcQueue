package com.yiko.ss.service;

import com.yiko.common.utils.PageUtils;
import com.yiko.ss.domain.AffairsDO;

import java.util.Map;

public interface AffairsService {
    PageUtils queryList(Map<String, Object> map);

    void save(AffairsDO affairsDO);

    AffairsDO get(String affairId);

    int update(AffairsDO affairsDO);

    int remove(String affairId);

    void insertSelective(AffairsDO affairsDO);


    int batchRemove(String[] affairId);

    Map<String, Object> showAddAffairPage();

    Map<String, Object> showEditAffairPage(String id);


    int updateByTemplateId(String templateId);

    int updateByTemplateIds(String[] templateIds);

    //同步事项接口
    void pullAffairs();


}
