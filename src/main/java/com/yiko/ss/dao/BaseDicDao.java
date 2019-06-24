package com.yiko.ss.dao;

import com.yiko.ss.domain.BaseDicDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseDicDao {

    int save(BaseDicDO record);

    int remove(String id);

    int batchRemove(String[] id);

    int update(BaseDicDO record);

    BaseDicDO get(Map<String, Object> map);

    List<BaseDicDO> list(Map<String, Object> map);

    int insertSelective(BaseDicDO baseDicDO);

//    List<BaseDicDO> chooseDept(@Param("baseDicType") String baseDicType,@Param("cName") String cName);


}