package com.yiko.ss.service.impl;

import com.yiko.common.enums.OnlineapplyStateEnum;
import com.yiko.ss.dao.OnlineApplyDao;
import com.yiko.ss.dao.UserInfoMapper;
import com.yiko.ss.dao.YmsRecordMapper;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.domain.UserInfo;
import com.yiko.ss.service.ChartAccoutService;
import com.yiko.ss.service.OnlineApplyService;
import com.yiko.ss.vo.ChartAccoutVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartAccoutServiceImpl implements ChartAccoutService {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    YmsRecordMapper ymsRecordMapper;
    @Autowired
    OnlineApplyService onlineApplyService;
    @Autowired
    OnlineApplyDao onlineApplyDao;


    @Override
    public ChartAccoutVo allDataByDate(Map<String, Object> params) {
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        //条件查询所有注册人数
        List<UserInfo> userInfoList = userInfoMapper.selectList(params);

        //提交事项人数
        List<OnlineApplyDO> onlineApplyDOList = commitOnlineList(startTime, endTime);

        //事项完结数
        List<OnlineApplyDO> doneOnlineApplyDOList = doneOnlineList(startTime, endTime);

        ChartAccoutVo chartAccoutVo = ChartAccoutVo.builder().
                registerUserCount(userInfoList.size()).commitAffairCount(onlineApplyDOList.size()).handlingMattersCount(doneOnlineApplyDOList.size()).build();
        return chartAccoutVo;
    }

    private List<OnlineApplyDO> commitOnlineList(String startTime, String endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("iscommit", "true");
        List<OnlineApplyDO> list = onlineApplyDao.countList(params);
        return list;
    }

    private List<OnlineApplyDO> doneOnlineList(String startTime, String endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("iscommit", "true");
        params.put("state", OnlineapplyStateEnum.YI_SHEN_HE.getMessage());
        List<OnlineApplyDO> list = onlineApplyDao.countList(params);
        return list;

    }
}
