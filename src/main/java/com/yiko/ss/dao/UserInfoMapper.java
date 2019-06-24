package com.yiko.ss.dao;

import com.yiko.ss.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserInfoMapper {
    int deleteByPrimaryKey(String id);

    UserInfo selectByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectList(Map<String, Object> map);

    UserInfo getById(Map<String, Object> map);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    int batchRemove(String[] id);

    List<UserInfo> queryUserByCreateDate(@Param("startTime") String startTime, @Param("endTime") String endTime);
}