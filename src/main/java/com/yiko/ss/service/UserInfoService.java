package com.yiko.ss.service;

import com.yiko.common.utils.PageUtils;
import com.yiko.ss.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserInfoService {
    PageUtils list(Map<String, Object> map);

    UserInfo getById(Map<String, Object> map);

    UserInfo selectByPrimaryKey(String id);

    int updateById(UserInfo userInfo);

    int remove(String id);

    int batchRemove(String[] id);

    List<UserInfo> queryUserByCreateDate(String startTime, String endTime);

    public boolean hasRole();
}
