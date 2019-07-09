package com.yiko.ss.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.ss.domain.BlackWhiteList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlackWhiteListDao extends MyMapper<BlackWhiteList> {

  BlackWhiteList selectById(String id);
  int removeById(String id);
  int insert(BlackWhiteList entry);
  int updateById(BlackWhiteList entry);
  List<BlackWhiteList> queryList(Map entry);


}
