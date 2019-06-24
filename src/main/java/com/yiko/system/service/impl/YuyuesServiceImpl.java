package com.yiko.system.service.impl;


import com.yiko.common.service.impl.BaseServiceImpl;
import com.yiko.system.dao.YuyueDao;
import com.yiko.system.domain.YuyueDO;
import com.yiko.system.service.YuyuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("yuyuesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YuyuesServiceImpl extends BaseServiceImpl<YuyueDO> implements YuyuesService {

	@Autowired
	private YuyueDao yuyueDao;

    @Override
    @PostConstruct//初始化时调用
    protected void setMapping()
    {
        this.setMapper(this.yuyueDao, YuyueDO.class);

    }


	@Override
	public List<YuyueDO> list(Map<String, Object> map) {

		Example example = new Example(YuyueDO.class);
		Example.Criteria criteria = example.createCriteria();
		/*if (StringUtils.hasValue(log.getUsername())) {
			criteria.andCondition("username=", log.getUsername());
		}
		if (StringUtils.hasValue(log.getOperation())) {
			criteria.andCondition("operation like", "%" + log.getOperation() + "%");
		}
		example.setOrderByClause("create_time");*/
		return this.selectByExample(example);

	}

	@Override
	public int count(Map<String, Object> map) {

		Example example = new Example(YuyueDO.class);
		Example.Criteria criteria = example.createCriteria();
		/*if (StringUtils.hasValue(log.getUsername())) {
			criteria.andCondition("username=", log.getUsername());
		}
		if (StringUtils.hasValue(log.getOperation())) {
			criteria.andCondition("operation like", "%" + log.getOperation() + "%");
		}
		example.setOrderByClause("create_time");*/
		return this.selectCountByExample(example);
	}



}
