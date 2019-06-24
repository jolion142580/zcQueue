package com.yiko.common.service.impl;

import com.yiko.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    protected Mapper<T> mapper;

    /**
     * T的Class对象
     */
    protected Class<T> tClass;

    public Mapper<T> getMapper() {
        return mapper;
    }

    /**
     * 抽象方法,用于子类继承,然后调用setBaseMapper方法设置BaseServiceImpl运行所需的参数
     */
    protected abstract void setMapping();

    /**
     * 在使用通用的service方法前需要,提供dao层的入口
     *
     * @param mapper dao层的入口
     * @param tClass T的Class对象
     */
    protected void setMapper(Mapper<T> mapper, Class<T> tClass) {
        this.mapper = mapper;
        this.tClass = tClass;
    }

    protected Class<T> gettClass() {
        return tClass;
    }

    protected void settClass(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    /**
     * 根据主键获取记录
     *
     * @param key
     * @return
     */
    @Override
    public T selectByKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(T entity) {
        return mapper.insertSelective(entity);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int delete(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int batchDelete(List<String> list, String property, Class<T> clazz) {
        Example example = new Example(clazz);
        Example.Criteria criteria =example.createCriteria();

        example.createCriteria().andIn(property, list);
        return this.mapper.deleteByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateAll(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateNotNull(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    @Override
    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }


}
