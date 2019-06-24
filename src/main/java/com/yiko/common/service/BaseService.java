package com.yiko.common.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<T> {

	List<T> selectAll();

	T selectByKey(Object key);

	int save(T entity);

	int delete(Object key);

	int batchDelete(List<String> list, String property, Class<T> clazz);

	int updateAll(T entity);

	int updateNotNull(T entity);

	List<T> selectByExample(Object example);

	//根据Exmaple条件查询总数
	int selectCountByExample(Object example);
}