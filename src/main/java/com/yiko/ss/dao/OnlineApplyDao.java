package com.yiko.ss.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.vo.OnlineApplyCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OnlineApplyDao extends MyMapper<OnlineApplyDO> {

    List<OnlineApplyDO> list(Map<String, Object> map);
    List<OnlineApplyDO> countList(Map<String, Object> map);

    int updateByCode(OnlineApplyDO onlineApplyDO);

    List<OnlineApplyCountVo> calculateDateTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    OnlineApplyDO queryUserNameAndAffairNameByOnlineId(String id);

}