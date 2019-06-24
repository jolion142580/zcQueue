package com.yiko.ss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.task.pullBean.PullAffairGuide;
import com.yiko.common.task.util.InterfaceUtil;
import com.yiko.common.utils.BDException;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.StringUtils;
import com.yiko.ss.dao.AffairGuideDao;
import com.yiko.ss.dao.AffairsDao;
import com.yiko.ss.domain.AffairGuideDO;
import com.yiko.ss.service.AffairGuideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AffairGuideServiceImpl implements AffairGuideService {
    @Autowired
    AffairGuideDao affairGuideDao;
    @Autowired
    AffairsDao affairsDao;
    @Autowired
    BootdoConfig bootdoConfig;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageUtils queryList(Map<String, Object> map) {
        Query query = new Query(map);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<AffairGuideDO> affairObjectDOList = affairGuideDao.list(map);
        PageInfo<AffairGuideDO> pageInfo = new PageInfo<>(affairObjectDOList);
        return new PageUtils(affairObjectDOList, new Long(pageInfo.getTotal()).intValue());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(AffairGuideDO affairGuideDO) {

        affairGuideDao.save(affairGuideDO);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public AffairGuideDO getAffairGuideByAffairId(String affairId) {
        return affairGuideDao.getByAffairId(affairId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertSelective(AffairGuideDO affairGuideDO) {
        int res = affairGuideDao.insertSelective(affairGuideDO);
        if (res < 1) {
            throw new BDException("插入办事指南数据失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int update(AffairGuideDO affairGuideDO) {
        return affairGuideDao.update(affairGuideDO);
    }

    @Override
    public int remove(String guideId) {
        return affairGuideDao.remove(guideId);
    }

    @Override
    public int batchRemove(String[] guideId) {
        return affairGuideDao.batchRemove(guideId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void pullAffairGuide() {
        //查询出全部affairId并放在set中
        Set<String> affairIdList = affairsDao.list(Maps.newHashMap())
                .stream().map(e -> e.getAffairId()).collect(Collectors.toSet());

        for (String affairId : affairIdList) {
            String realUrl = bootdoConfig.getPullAffairGuide() + affairId;

            PullAffairGuide pullAffairGuide = InterfaceUtil.parseJsonToBeanByDataSimple(PullAffairGuide.class, realUrl);
            if (null == pullAffairGuide || StringUtils.isBlank(pullAffairGuide.getGUIDEID())) {
                continue;
            }

            AffairGuideDO assignment = PullAffairGuide2AffairsGuide(pullAffairGuide);

            if (null == assignment) {
                throw new BDException("获取办事指南数据失败,对象为空");
            }
            AffairGuideDO affairGuideDO = affairGuideDao.selectByPrimaryKey(assignment.getGuideId());
            //更新
            if (null != affairGuideDO) {
                int res = update(assignment);
                if (res < 1) {
                    throw new BDException("更新办事指南数据失败");
                }
            }
            //插入
            else {
                int res = affairGuideDao.insertSelective(assignment);
                if (res < 1) {
                    throw new BDException("插入办事指南数据失败");
                }
            }
        }
        log.info("【办事指南】同步办事指南数据结束");

    }

    private AffairGuideDO PullAffairGuide2AffairsGuide(PullAffairGuide pullAffairGuide) {
        AffairGuideDO affairGuideDO = AffairGuideDO.builder()
                .affairId(pullAffairGuide.getAFFAIRID() == null ? "" : pullAffairGuide.getAFFAIRID())
                .guideId(pullAffairGuide.getGUIDEID() == null ? "" : pullAffairGuide.getGUIDEID())
                .accessoryPath(pullAffairGuide.getACCESSORYPATH() == null ? "" : pullAffairGuide.getACCESSORYPATH())
                .according(pullAffairGuide.getACCORDING() == null ? "" : pullAffairGuide.getACCORDING())
                .charge(pullAffairGuide.getCHARGE() == null ? "" : pullAffairGuide.getCHARGE())
                .chargegist(pullAffairGuide.getCHARGEGIST() == null ? "" : pullAffairGuide.getCHARGEGIST())
                .condition(pullAffairGuide.getCONDITION() == null ? "" : pullAffairGuide.getCONDITION())
                .inquire(pullAffairGuide.getINQUIRE() == null ? "" : pullAffairGuide.getINQUIRE())
                .institution(pullAffairGuide.getINSTITUTION() == null ? "" : pullAffairGuide.getINSTITUTION())
                .material(pullAffairGuide.getMATERIAL() == null ? "" : pullAffairGuide.getMATERIAL())
                .procedures(pullAffairGuide.getPROCEDURES() == null ? "" : pullAffairGuide.getPROCEDURES())
                .sepcialversion(pullAffairGuide.getSEPCIALVERSION() == null ? "" : pullAffairGuide.getSEPCIALVERSION())
                .site(pullAffairGuide.getSITE() == null ? "" : pullAffairGuide.getSITE())
                .time(pullAffairGuide.getTIME() == null ? "" : pullAffairGuide.getTIME())
                .xrndomu(pullAffairGuide.getXRNDOMU() == null ? "" : pullAffairGuide.getXRNDOMU())
                .build();

        return affairGuideDO;

    }
}
