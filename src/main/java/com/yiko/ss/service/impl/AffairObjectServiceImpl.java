package com.yiko.ss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.domain.Tree;
import com.yiko.common.exception.BDException;
import com.yiko.common.task.pullBean.PullAffairObject;
import com.yiko.common.task.util.InterfaceUtil;
import com.yiko.common.utils.BuildTree;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.ss.dao.AffairObjectMapper;
import com.yiko.ss.dao.AffairsDao;
import com.yiko.ss.dao.BaseDicDao;
import com.yiko.ss.dao.TemplateFileMapper;
import com.yiko.ss.domain.AffairObject;
import com.yiko.ss.domain.AffairsDO;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.domain.TemplateFile;
import com.yiko.ss.service.AffairObjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AffairObjectServiceImpl implements AffairObjectService {
    @Autowired
    AffairObjectMapper affairObjectDao;

    @Autowired
    AffairsDao affairsDao;

    @Autowired
    BaseDicDao baseDicDao;

    @Autowired
    TemplateFileMapper templateFileMapper;

    @Autowired
    BootdoConfig bootdoConfig;

    @Override
    public Tree<Object> getTree() {
        List<Tree<Object>> trees = new ArrayList<>();
        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("baseDicType", "depart");
        List<BaseDicDO> baseDicDOList = baseDicDao.list(baseMap);

        Map<String, Object> affairTypeMap = new HashMap<>();


        //遍历所有部门
        for (BaseDicDO baseDicDO : baseDicDOList) {
            //新建一棵树，把每个部门对象保存到树
            Tree<Object> tree = new Tree<>();
            tree.setId(baseDicDO.getId());
            tree.setText(baseDicDO.getcName());

            //每个部门的baseDicId
            List<Tree<Object>> childrenList = new ArrayList<>();

            Map<String, Object> departMap = new HashMap<>();
            departMap.put("departId", baseDicDO.getBaseDicId());
            departMap.put("baseDicType", "depart");
            List<AffairsDO> affairsDOList = affairsDao.listByCname(departMap);
            for (AffairsDO affairsDO : affairsDOList) {

                Tree<Object> affairsDOTree = new Tree<>();
                affairsDOTree.setText(affairsDO.getAffairName());
                affairsDOTree.setId(affairsDO.getAffairId());
                affairsDOTree.setParentId(baseDicDO.getId());
                childrenList.add(affairsDOTree);
                Map<String, Object> state = new HashMap<>(16);
                state.put("opened", false);
                state.put("mType", "depart");
                tree.setState(state);

            }
            tree.setChildren(childrenList);
            trees.add(tree);

        }
        Tree<Object> t = BuildTree.build(trees);
        return t;

    }

    @Override
    public List<AffairObject> list(Map<String, Object> map) {
        return affairObjectDao.list(map);
    }

    @Override
    public AffairObject get(String objId) {
        return affairObjectDao.selectByPrimaryKey(objId);
    }

    @Override
    public PageUtils pageList(Map<String, Object> map) {
        Query query = new Query(map);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<AffairObject> affairObjectDOList = affairObjectDao.list(map);
        PageInfo<AffairObject> pageInfo = new PageInfo<>(affairObjectDOList);
        return new PageUtils(affairObjectDOList, new Long(pageInfo.getTotal()).intValue());
    }

    @Override
    public int update(AffairObject affairObject) {
        return affairObjectDao.updateByPrimaryKeySelective(affairObject);
    }

    @Override
    public int remove(String objId) {
        return affairObjectDao.deleteByPrimaryKey(objId);
    }

    @Override
    public int batchRemove(String[] objId) {
        return affairObjectDao.batchRemove(objId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> updateAffairType(String objId) {
        AffairObject affairObject = affairObjectDao.selectByPrimaryKey(objId);
        Preconditions.checkNotNull(affairObject);
        String affairId = affairObject.getAffairid();
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isNotBlank(affairId)) {
            AffairsDO affairsDO = affairsDao.get(affairId);
            Preconditions.checkNotNull(affairsDO);
            Map map = new HashMap();
            map.put("departid", affairsDO.getDepartId());
            List<TemplateFile> templateFileList = templateFileMapper.list(map);
            for (TemplateFile templateFile : templateFileList) {
                if (templateFile.getId().equals(affairObject.getTemplateid())) {
                    templateFile.setIsCheck("check");
                }
                if (templateFile.getId().equals(affairObject.getTemplateid1())) {
                    templateFile.setTemplateId1IsChecked("check");
                }
            }
            returnMap.put("affairobject", affairObject);
            returnMap.put("templateFileList", templateFileList);
        } else {
            throw new RuntimeException("事项分类affairId不存在");
        }
        return returnMap;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void pullAffairObject() {
        //查询出全部affairId并放在List中
        List<String> affairIdList = affairsDao.list(Maps.newHashMap())
                .stream().map(e -> e.getAffairId()).collect(Collectors.toList());
        for (String affairId : affairIdList) {
            String realUrl = bootdoConfig.getPullAffairObject() + affairId;
            List<PullAffairObject> pullAffairObjects = InterfaceUtil.parseJsonToBeanByData(PullAffairObject.class, realUrl);
            if (CollectionUtils.isNotEmpty(pullAffairObjects)) {
                for (PullAffairObject pullAffairObject : pullAffairObjects) {
                    AffairObject assignment = pullAffairObject2AffairObject(pullAffairObject);
                    assignment.setAffairid(affairId);
                    AffairObject queryObject = this.get(assignment.getObjid());
                    //数据库不存在，插入
                    if (null == queryObject) {
                        int res = affairObjectDao.insertSelective(assignment);
                        if (res < 1) {
                            throw new BDException("【同步事项对象】数据插入异常");
                        }
                    }
                    //更新
                    else {
                        int res = affairObjectDao.updateByPrimaryKeySelective(assignment);
                        if (res < 1) {
                            throw new BDException("【同步事项对象】数据更新异常");
                        }
                    }
                }
            }
            else{
                log.info("【同步事项对象】根据事项id{}找不到对应的事项对象",affairId);
            }
        }
        log.info("============同步事项对象结束==================");
    }


    //把接口同步的bean转换为数据对应的bean
    private AffairObject pullAffairObject2AffairObject(PullAffairObject pullAffairObject) {
        AffairObject affairObject = AffairObject.builder()
                .objid(pullAffairObject.getOBJID())
                .objname(pullAffairObject.getOBJNAME())
                .objindex(pullAffairObject.getOBJINDEX())
                .build();
        return affairObject;
    }
}
