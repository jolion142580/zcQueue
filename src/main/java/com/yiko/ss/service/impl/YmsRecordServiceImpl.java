package com.yiko.ss.service.impl;

import com.yiko.common.service.impl.BaseServiceImpl;
import com.yiko.common.utils.StringUtils;
import com.yiko.ss.dao.YmsRecordMapper;
import com.yiko.ss.domain.YmsRecord;
import com.yiko.ss.service.YmsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class YmsRecordServiceImpl extends BaseServiceImpl<YmsRecord> implements YmsRecordService {

    @Autowired
    YmsRecordMapper ymsRecordMapper;

    @Override
    public List<YmsRecord> listByNotNull(YmsRecord ymsRecord) {
        return ymsRecordMapper.listByNotNull(ymsRecord);
    }

    @Override
    protected void setMapping() {
      this.setMapper(this.ymsRecordMapper,YmsRecord.class);
    }
    /*@Override


    @Override
    protected void setMapper(Mapper<YmsRecord> mapper, Class<YmsRecord> ymsRecordClass) {
        super.setMapper(mapper, ymsRecordClass);
    }

    @Override
    protected Class<YmsRecord> gettClass() {
        return super.gettClass();
    }

    @Override
    protected void settClass(Class<YmsRecord> ymsRecordClass) {
        super.settClass(ymsRecordClass);
    }

    @Override
    public List<YmsRecord> selectAll() {
        return super.selectAll();
    }

    @Override
    public YmsRecord selectByKey(Object key) {
        return super.selectByKey(key);
    }

    @Override
    public int save(YmsRecord entity) {
        return super.save(entity);
    }

    @Override
    public int delete(Object key) {
        return super.delete(key);
    }

    @Override
    public int batchDelete(List<String> list, String property, Class<YmsRecord> clazz) {
        return super.batchDelete(list, property, clazz);
    }

    @Override
    public int updateAll(YmsRecord entity) {
        return super.updateAll(entity);
    }

    @Override
    public int updateNotNull(YmsRecord entity) {
        return super.updateNotNull(entity);
    }

    @Override
    public List<YmsRecord> selectByExample(Object example) {
        return super.selectByExample(example);
    }

    @Override
    public int selectCountByExample(Object example) {
        return super.selectCountByExample(example);
    }*/

    @Override
    public List<YmsRecord> handlingMatters() {
        return ymsRecordMapper.selectAll();
    }

    @Override
    public List<YmsRecord> queryHandlingMattersByDate(String startTime, String endTime) {
        return ymsRecordMapper.queryHandlingMattersByDate(startTime, endTime);
    }
}
