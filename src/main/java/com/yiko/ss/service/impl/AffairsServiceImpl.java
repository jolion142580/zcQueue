package com.yiko.ss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.enums.SSEnum;
import com.yiko.common.exception.BDException;
import com.yiko.common.exception.BootDoException;
import com.yiko.common.task.pullBean.PullAffairs;
import com.yiko.common.task.util.InterfaceUtil;
import com.yiko.common.utils.KeyUtil;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.StringUtils;
import com.yiko.ss.dao.*;
import com.yiko.ss.domain.AffairGuideDO;
import com.yiko.ss.domain.AffairsDO;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.domain.TemplateFile;
import com.yiko.ss.service.AffairsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AffairsServiceImpl implements AffairsService {
    @Autowired
    AffairsDao affairsDao;
    @Autowired
    BaseDicDao baseDicDao;
    @Autowired
    AffairGuideDao affairGuideDao;
    @Autowired
    AffairMaterialsMapper affairMaterialsMapper;
    @Autowired
    AffairObjectMapper affairObjectMapper;

    @Autowired
    TemplateFileMapper templateFileMapper;
    @Autowired
    BootdoConfig bootdoConfig;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageUtils queryList(Map<String, Object> map) {


        //根据部门名称查询
        String departName = (String) map.get("departName");
        if (StringUtils.isNotBlank(departName)) {
            Map<String, Object> depart = new HashMap<>();
            depart.put("cName", departName);
            List<BaseDicDO> baseDicDOList = baseDicDao.list(depart);
            List<String> departIds = baseDicDOList.stream().map(e -> e.getBaseDicId()).collect(Collectors.toList());
            map.put("departIds", departIds);

            //部门
            map.put("baseDicType", "depart");
        }

        //设置列表的部门名称和人生事项所需的map
        Query query = new Query(map);
        Map<String, Object> deparMap = new HashMap<>();
        deparMap.put("baseDicType", "depart");
        Map<String, Object> lifeMap = new HashMap<>();
        lifeMap.put("baseDicType", "life");

        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<AffairsDO> affairsDOList = affairsDao.list(map);
        for (int i = 0; i < affairsDOList.size(); i++) {

            AffairsDO affairsDO = affairsDOList.get(i);
            String sortId = affairsDO.getSortId();
            String departId = affairsDO.getDepartId();

            if (StringUtils.isNotBlank(departId)) {
                deparMap.put("baseDicId", departId);
                BaseDicDO baseDO = baseDicDao.get(deparMap);
                if (null != baseDO && null != baseDO.getcName())
                    affairsDO.setDepartName(baseDO.getcName());
            } else {
                affairsDO.setDepartName("");
            }

            if (StringUtils.isNotBlank(sortId)) {
                lifeMap.put("baseDicId", sortId);
                BaseDicDO baseDO = baseDicDao.get(lifeMap);
                if (null != baseDO && null != baseDO.getcName())
                    affairsDO.setLifeName(baseDO.getcName());
            } else {
                affairsDO.setLifeName("");
            }
        }
        PageInfo<AffairsDO> pageInfo = new PageInfo<>(affairsDOList);
        return new PageUtils(affairsDOList, new Long(pageInfo.getTotal()).intValue());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(AffairsDO affairsDO) {
        String affairId = affairsDO.getAffairId();
        AffairGuideDO affairGuideDO = new AffairGuideDO();
        affairGuideDO.setGuideId(KeyUtil.genUniqueKey());
        affairGuideDO.setAffairId(affairId);

        affairsDao.insertSelective(affairsDO);
        affairGuideDao.insertSelective(affairGuideDO);
    }

    @Override
    public AffairsDO get(String affairId) {
        return affairsDao.get(affairId);
    }

    @Override
    public int update(AffairsDO affairsDO) {
        return affairsDao.update(affairsDO);
    }

    @Override
    public int remove(String affairId) {
        return affairsDao.remove(affairId);
    }

    @Override
    public int batchRemove(String[] affairId) {
        return affairsDao.batchRemove(affairId);
    }

    @Override
    public int updateByTemplateId(String templateId) {
        return affairsDao.updateByTemplateId(templateId);
    }

    @Override
    public int updateByTemplateIds(String[] templateIds) {
        return affairsDao.updateByTemplateIds(templateIds);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> showAddAffairPage() {
        Map departMap = new HashMap();
        departMap.put("baseDicType", "depart");
        Map lifeMap = new HashMap();
        lifeMap.put("baseDicType", "life");
        //部门列表
        List<BaseDicDO> departList = baseDicDao.list(departMap);
        //人生事项列表
        List<BaseDicDO> lifeList = baseDicDao.list(lifeMap);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("departList", departList);
        returnMap.put("lifeList", lifeList);

        return returnMap;
    }

    @Override
    public Map<String, Object> showEditAffairPage(String id) {
        AffairsDO affairsDO = affairsDao.get(id);
        if (affairsDO == null) {
            throw new BootDoException(SSEnum.FIND_ERROR);
        }
        Map departMap = new HashMap();
        departMap.put("baseDicType", "depart");
        Map lifeMap = new HashMap();
        lifeMap.put("baseDicType", "life");

        //事项信息
        List<BaseDicDO> lifeList = null;
        if (affairsDO.getSortId() != null && !affairsDO.equals("")) {
            lifeList = baseDicDao.list(lifeMap);
            for (BaseDicDO baseDicDO : lifeList) {
                if (baseDicDO.getBaseDicId().equals(affairsDO.getSortId())) {
                    baseDicDO.setIsCheck("check");
                }
            }
        }

        //事项主题分类
        List<BaseDicDO> departList = null;
        if (affairsDO.getDepartId() != null && !affairsDO.getDepartId().equals("")) {
            departList = baseDicDao.list(departMap);
            for (BaseDicDO baseDicDO : departList) {
                if (baseDicDO.getBaseDicId().equals(affairsDO.getDepartId())) {
                    baseDicDO.setIsCheck("check");
                }
            }
        }

        //模板信息
        Map fileListMap = new HashMap();
        fileListMap.put("departid", affairsDO.getDepartId());
        List<TemplateFile> templateFileList = templateFileMapper.list(fileListMap);
        for (TemplateFile templateFile : templateFileList) {
            if (templateFile.getId().equals(affairsDO.getTemplateId())) {
                templateFile.setIsCheck("check");
            }
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("affairs", affairsDO);
        returnMap.put("fileList", templateFileList);
        returnMap.put("departList", departList);
        returnMap.put("lifeList", lifeList);
        return returnMap;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertSelective(AffairsDO affairsDO) {
        int res = affairsDao.insertSelective(affairsDO);
        if (res < 1) {
            throw new BDException("保存事项失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void pullAffairs() {
        List<PullAffairs> pullAffairList = InterfaceUtil.parseJsonToBeanByData(PullAffairs.class, bootdoConfig.getPullAffairs());
        if (CollectionUtils.isEmpty(pullAffairList)) {
            throw new BDException("===============事项接口数据获取失败====================");
        }
        for (PullAffairs pullAffairs : pullAffairList) {
            //根据id查询事项是否存在，如存在则更新，不存在则插入
            AffairsDO queryAffairDO = this.get(pullAffairs.getId());

            //转换为数据库对象
            AffairsDO affairsDO = pullAffairs2AffairsDO(pullAffairs);

            //更新
            if (null != queryAffairDO) {
                int res = this.update(affairsDO);
                if (res < 1) {
                    throw new BDException("===============更新事项数据异常====================");
                }
            }
            //插入
            else {
                this.insertSelective(affairsDO);
            }

        }

        //删除多余数据  -->同步接口没有，本地数据库有的数据，则删除

        //查找本地数据
        List<AffairsDO> affairsDOList = affairsDao.list(Maps.newHashMap());

        //删除标志  false代表不删除  true删除
        boolean flag = true;

        //本地数据
        for (AffairsDO affairsDO : affairsDOList) {
            //同步接口数据
            for (PullAffairs pullAffairs : pullAffairList) {
                if (affairsDO.getAffairId().equals(pullAffairs.getId())) {
                    flag = false;
                    log.info("【同步事项】没有多余数据，不做删除");
                    break;
                }
            }
            if (flag) {
                log.info("【同步事项】有多余数据，做删除");
                String affairId = affairsDO.getAffairId();
                //事项表
                this.remove(affairId);
                //事项对象表
                affairObjectMapper.deleteByAffairId(affairId);
                //事项材料表
                affairMaterialsMapper.deleteByAffairId(affairId);
                //事项指南表
                affairGuideDao.deleteByAffairId(affairId);

            }
        }

        log.info("【同步事项】同步事项结束");


    }

    //同步事项接口的bean转换为数据库对应的bean
    private AffairsDO pullAffairs2AffairsDO(PullAffairs pullAffairs) {
        AffairsDO affairsDO = AffairsDO.builder()
                .affairId(pullAffairs.getId())
                .departId(pullAffairs.getDepartid() == null ? "" : pullAffairs.getDepartid())
                .level(pullAffairs.getLevel() == null ? "" : pullAffairs.getLevel())
                .timeLimitType(pullAffairs.getTimelimit_type() == null ? "" : pullAffairs.getTimelimit_type())
                .timeLimit(pullAffairs.getTimelimit() == null ? "" : pullAffairs.getTimelimit())
                .sortId(pullAffairs.getSortid() == null ? "" : pullAffairs.getSortid())
                .resultForm(pullAffairs.getResult_form() == null ? "" : pullAffairs.getResult_form())
                .isNet(pullAffairs.getIsnet() == null ? "" : pullAffairs.getIsnet())
                .code(pullAffairs.getCode() == null ? "" : pullAffairs.getCode())
                .affairName(pullAffairs.getAffairname() == null ? "" : pullAffairs.getAffairname())
                .isTodo(pullAffairs.getIstodo() == null ? "" : pullAffairs.getIstodo())
                .build();
        return affairsDO;
    }
}
