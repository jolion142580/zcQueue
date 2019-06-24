package com.yiko.system.dao;

import com.yiko.common.config.MyMapper;
import com.yiko.system.domain.ShortMessage;

public interface ShortMessageMapper extends MyMapper<ShortMessage> {
    int sameDayCount(String ipAddress);
}