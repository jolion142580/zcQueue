package com.yiko.ss.service;

import com.yiko.ss.domain.BaseDicDO;

import java.util.List;
import java.util.Map;

public interface BaseDicService {
    List<BaseDicDO> queryList(Map<String, Object> params);

    int save(BaseDicDO baseDicDO);

    BaseDicDO get(Map<String, Object> map);

    int update(BaseDicDO baseDicDO);

    int remove(String id);

    int batchRemove(String[] id);

    //同步部门接口数据
    void pullDepart();

//    List<BaseDicDO> chooseDept(String baseDicType,String cName);
}
