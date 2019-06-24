package com.yiko.ss.service;

import com.yiko.common.utils.R;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.domain.OnlineApplyOpinion;

import java.util.List;

public interface OnlineApplyOpinionService {

    R addOpinion(OnlineApplyDO onlineApplyDO);

    List<OnlineApplyOpinion> selectListByOnlineAppId(String onlineAppId);

    int countTotalByOnlineAppId(String onlineAppId);
}
