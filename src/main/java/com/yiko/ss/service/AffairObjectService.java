package com.yiko.ss.service;

import com.yiko.common.domain.Tree;
import com.yiko.common.utils.PageUtils;
import com.yiko.ss.domain.AffairObject;
import com.yiko.ss.domain.BaseDicDO;

import java.util.List;
import java.util.Map;

public interface AffairObjectService {


    Tree<Object> getTree();

    AffairObject get(String objId);

    PageUtils pageList(Map<String, Object> map);

    int update(AffairObject affairObject);

    int remove(String objId);

    int batchRemove(String[] objId);

    Map<String, Object> updateAffairType(String objId);

    List<AffairObject> list(Map<String, Object> map);

    void pullAffairObject();

}
