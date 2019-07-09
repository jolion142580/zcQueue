package com.yiko.ss.service;

import com.yiko.common.service.BaseService;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.BlackWhiteList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BlackWhiteListService extends BaseService<BlackWhiteList>
{
    BlackWhiteList selectById(String id);
    R removeById(String id);
    R insert(BlackWhiteList entry);
    R updateById(BlackWhiteList entry);
    PageUtils queryList(Map entry);

}
