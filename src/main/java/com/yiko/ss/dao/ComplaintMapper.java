package com.yiko.ss.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.ss.domain.Complaint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ComplaintMapper {
    int deleteByPrimaryKey(String cId);

    int insert(Complaint record);

    int insertSelective(Complaint record);

    Complaint selectByPrimaryKey(String cId);

    int updateByPrimaryKeySelective(Complaint record);

    int updateByPrimaryKey(Complaint record);

    List<Complaint> list(Map<String, Object> map);

    int batchRemove(String[] cId);
}