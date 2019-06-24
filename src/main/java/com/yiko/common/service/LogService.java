package com.yiko.common.service;

import com.yiko.common.utils.Query;
import org.springframework.stereotype.Service;

import com.yiko.common.domain.LogDO;
import com.yiko.common.domain.PageDO;

@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
