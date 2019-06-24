package com.yiko.ss.service.impl;

import com.google.common.collect.Maps;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.exception.BDException;
import com.yiko.common.task.pullBean.PullDepart;
import com.yiko.common.task.util.InterfaceUtil;
import com.yiko.common.utils.KeyUtil;
import com.yiko.ss.dao.BaseDicDao;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.service.BaseDicService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BaseDicServiceImpl implements BaseDicService {
    @Autowired
    private BaseDicDao baseDicDao;

    @Autowired
    private BootdoConfig bootdoConfig;

    @Override
    public List<BaseDicDO> queryList(Map<String, Object> params) {
        return baseDicDao.list(params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int save(BaseDicDO baseDicDO) {
        return baseDicDao.save(baseDicDO);
    }

    @Override
    public BaseDicDO get(Map<String, Object> map) {
        return baseDicDao.get(map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int update(BaseDicDO baseDicDO) {
        return baseDicDao.update(baseDicDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int remove(String id) {
        return baseDicDao.remove(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int batchRemove(String[] id) {
        return baseDicDao.batchRemove(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void pullDepart() {
        List<PullDepart> list = InterfaceUtil.parseJsonToBeanByData(PullDepart.class, bootdoConfig.getPullDepart());

        Map<String, Object> queryMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(list)) {
            throw new BDException("===============部门接口数据获取失败====================");
        }
        for (PullDepart pullDepart : list) {
            BaseDicDO baseDicDO = new BaseDicDO();
            baseDicDO.setBaseDicId(pullDepart.getId());
            baseDicDO.setcName(pullDepart.getDepart_name());


            queryMap.put("baseDicType", "depart");
            queryMap.put("baseDicId", pullDepart.getId());
            BaseDicDO baseDicDORes = baseDicDao.get(queryMap);

            //数据已存在，更新操作
            if (null != baseDicDORes) {
                baseDicDO.setId(baseDicDORes.getId());
                int res = baseDicDao.update(baseDicDO);
                if (res < 1) {
                    throw new BDException("===============部门接口数据获取失败,更新数据出错====================");
                }
            }
            //插入新数据
            else {
                baseDicDO.setId(KeyUtil.genUniqueKey());
                baseDicDO.setValid("1");
                baseDicDO.setBaseDicType("depart");
                int res = baseDicDao.insertSelective(baseDicDO);
                if (res < 1) {
                    throw new BDException("===============部门接口数据获取失败,插入数据出错====================");
                }

            }

        }

        /**
         *        删除多余数据
         */
        Map<String, Object> departMap = Maps.newHashMap();
        //false 代表不删除， true 删除
        boolean flag = true;
        departMap.put("baseDicType", "depart");

        //根据departMap查询出部门列表
        List<BaseDicDO> departList = this.queryList(departMap);

        //如果本地有数据，接口中没有这条记录，则删除
        for (BaseDicDO baseDicDO : departList) {
            for (PullDepart pullDepart : list) {
                if (baseDicDO.getBaseDicId().equals(pullDepart.getId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                this.remove(baseDicDO.getId());
            }
        }
    }
}
