package com.yiko.common.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.dao.LogDao;
import com.yiko.common.domain.PageDO;
import com.yiko.common.service.LogService;
import com.yiko.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yiko.common.domain.LogDO;

@Service
public class LogServiceImpl implements LogService {
	@Autowired
    LogDao logMapper;

	@Async
	@Override
	public void save(LogDO logDO) {
		 logMapper.save(logDO);
	}

	@Override
	public PageDO<LogDO> queryList(Query query) {
		/*int total = logMapper.count(query);
		List<LogDO> logs = logMapper.list(query);
		PageDO<LogDO> page = new PageDO<>();
		page.setTotal(total);
		page.setRows(logs);
		return page;*/
		PageDO<LogDO> page = new PageDO<>();
		PageHelper.startPage(query.getPageNumber(),query.getPageSize());
		List<LogDO> logs = logMapper.list(query);
		PageInfo<LogDO> pageInfo=new PageInfo<>(logs);
		page.setTotal(new Long(pageInfo.getTotal()).intValue());
		page.setRows(logs);
		return page;
	}

	@Override
	public int remove(Long id) {
		int count = logMapper.remove(id);
		return count;
	}

	@Override
	public int batchRemove(Long[] ids){
		return logMapper.batchRemove(ids);
	}
}
