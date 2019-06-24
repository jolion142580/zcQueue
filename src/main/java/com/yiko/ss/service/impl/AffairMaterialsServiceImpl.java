package com.yiko.ss.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.domain.Tree;
import com.yiko.common.exception.BDException;
import com.yiko.common.task.pullBean.PullAffairMaterials;
import com.yiko.common.task.util.InterfaceUtil;
import com.yiko.common.utils.*;
import com.yiko.ss.dao.AffairMaterialsMapper;
import com.yiko.ss.dao.AffairObjectMapper;
import com.yiko.ss.dao.AffairsDao;
import com.yiko.ss.dao.BaseDicDao;
import com.yiko.ss.domain.AffairMaterials;
import com.yiko.ss.domain.AffairObject;
import com.yiko.ss.domain.AffairsDO;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.service.AffairMaterialsService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AffairMaterialsServiceImpl implements AffairMaterialsService {
    @Autowired
    AffairMaterialsMapper affairMaterialsMapper;

    @Autowired
    BootdoConfig bootdoConfig;

    @Autowired
    BaseDicDao baseDicDao;

    @Autowired
    AffairsDao affairsDao;

    @Autowired
    AffairObjectMapper affairObjectMapper;

    @Override
    public PageUtils queryList(Map<String, Object> map) {
        String objId = (String) map.get("objId");
        if (objId.indexOf("objid") != -1) {
            String newObjId = objId.substring(5);
            AffairObject affairObject = affairObjectMapper.selectByPrimaryKey(newObjId);
            Preconditions.checkNotNull(affairObject);
            Query query = new Query(map);
            PageHelper.startPage(query.getPageNumber(), query.getPageSize());
            List<AffairMaterials> affairMaterialsList = affairMaterialsMapper.selectAffairMaterials(affairObject.getAffairid(), affairObject.getObjindex());
            PageInfo<AffairMaterials> pageInfo = new PageInfo<>(affairMaterialsList);
            return new PageUtils(affairMaterialsList, new Long(pageInfo.getTotal()).intValue());
        }
        return new PageUtils(new ArrayList<>(), 0);

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R save(String affairMaterials, String objId, MultipartFile file) {

        String id = KeyUtil.genUniqueKey();
        JSONObject jsonObject = JSONObject.fromObject(affairMaterials);
        AffairMaterials saveAffairMaterials = (AffairMaterials) JSONObject.toBean(jsonObject, AffairMaterials.class);
        saveAffairMaterials.setId(id);
        AffairObject affairObject = affairObjectMapper.selectByPrimaryKey(objId);

        saveAffairMaterials.setMatindex(affairObject.getObjindex());

        saveAffairMaterials.setAffairid(affairObject.getAffairid());

        if (file != null) {
            boolean flag = FileUtil.isSuccessFileName(file.getOriginalFilename());
            if (!flag) {
                return R.error("只能上传.doc  .docx  .xls  类型的文件!");
            }
            String fileName = file.getOriginalFilename();
            String saveName = KeyUtil.genUniqueKey();
            String localPath = bootdoConfig.getUploadMateriasTemplate() + saveName + fileName;
            saveAffairMaterials.setLocalpath(localPath);
            try {
                FileUtil.uploadFile(file.getBytes(), bootdoConfig.getUploadMateriasTemplate(), fileName);
            } catch (Exception e) {
                return R.error();
            }
        }
        int result = affairMaterialsMapper.insertSelective(saveAffairMaterials);

        if (result > 0) {
            return R.ok();
        }
        return R.error("保存事项材料出错");
    }

    @Override
    public AffairMaterials selectOneByCondition(Map<String, Object> map) {
        return affairMaterialsMapper.selectOne(map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int remove(String id) {
        return affairMaterialsMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int removePath(String id) {
        AffairMaterials affairMaterials = affairMaterialsMapper.selectByPrimaryKey(id);
        String path = affairMaterials.getLocalpath();
        if (StringUtils.isNotBlank(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        return affairMaterialsMapper.SetLocalPathByNull(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int batchRemove(String[] id) {
        return affairMaterialsMapper.batchRemove(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R update(String affairMaterials, MultipartFile file) {
        JSONObject jsonObject = JSONObject.fromObject(affairMaterials);
        AffairMaterials affairMaterials1 = (AffairMaterials) JSONObject.toBean(jsonObject, AffairMaterials.class);

        if (file != null) {
            String fileName = file.getOriginalFilename();
            boolean flag = FileUtil.isSuccessFileName(fileName);

            if (!flag) {
                return R.error("只能上传.doc  .docx  .xls  类型的文件!");
            }
            String saveName = KeyUtil.genUniqueKey();

            String localPath = bootdoConfig.getUploadMateriasTemplate() + saveName + fileName;
            affairMaterials1.setLocalpath(localPath);


            try {
                FileUtil.uploadFile(file.getBytes(), bootdoConfig.getUploadMateriasTemplate(), saveName + fileName);
            } catch (Exception e) {
                return R.error();
            }
        }
        int result = affairMaterialsMapper.updateByPrimaryKeySelective(affairMaterials1);
        if (result <= 0) {
            return R.error();
        }
        return R.ok();


    }


    public Tree<Object> getTreeAndAffairType() {
        //树
        List<Tree<Object>> trees = new ArrayList<>();

        //查询全部部门
        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("baseDicType", "depart");
        List<BaseDicDO> baseDicDOList = baseDicDao.list(baseMap);

        //遍历部门
        for (BaseDicDO baseDicDO : baseDicDOList) {

            //把每一个部门保存到一个树中
            Tree<Object> baseTree = new Tree<>();
            baseTree.setId(baseDicDO.getId());
            baseTree.setText(baseDicDO.getcName());

            //每一个部门下的每一个事项（部门下的子树）
            List<Tree<Object>> affairTreeList = new ArrayList<>();

            //每一个事项下的事项分类
            List<Tree<Object>> affairTypeTreeList = null;

            //根据部门的id查询对应的事项
            Map<String, Object> affairtMap = new HashMap<>();
            affairtMap.put("departId", baseDicDO.getBaseDicId());
            affairtMap.put("baseDicType", "depart");
            List<AffairsDO> affairsDOList = affairsDao.listByCname(affairtMap);

            //遍历每个事项，查询相应事项分类
            for (AffairsDO affairsDO : affairsDOList) {
                affairTypeTreeList = Lists.newArrayList();
                Tree<Object> affairTree = new Tree<>();
                affairTree.setId(affairsDO.getAffairId());
                affairTree.setText(affairsDO.getAffairName());


                Map<String, Object> affairtObjectMap = new HashMap<>();
                affairtObjectMap.put("affairId", affairsDO.getAffairId());
                List<AffairObject> affairObjectList = affairObjectMapper.list(affairtObjectMap);
                for (AffairObject affairObject : affairObjectList) {
                    Tree<Object> affairTypeTree = new Tree<>();
                    affairTypeTree.setId("objid" + affairObject.getObjid());
                    affairTypeTree.setText(affairObject.getObjname());
                    affairTypeTreeList.add(affairTypeTree);
                }

                affairTree.setChildren(affairTypeTreeList);

                affairTreeList.add(affairTree);

            }

            baseTree.setChildren(affairTreeList);
            trees.add(baseTree);
        }
        Tree<Object> t = BuildTree.build(trees);
        return t;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void pullAffairMaterials() {
        List<AffairObject> affairObjectList = affairObjectMapper.list(Maps.newHashMap());
        for (AffairObject affairObject : affairObjectList) {
            //请求地址
            String url = String.format("%s&affair_id=%s&obj_index=%s", bootdoConfig.getPullAffairMaterials(), affairObject.getAffairid(), affairObject.getObjindex());
            //返回的集合
            List<PullAffairMaterials> pullAffairMaterialsList = InterfaceUtil.parseJsonToBeanByData(PullAffairMaterials.class, url);

            if (CollectionUtils.isNotEmpty(pullAffairMaterialsList)) {
                //相同的参数在本地查询，如果请求返回中没有，则删除本地有的
                List<AffairMaterials> localList = affairMaterialsMapper.selectAffairMaterials(affairObject.getAffairid(), affairObject.getObjindex());
                if (CollectionUtils.isNotEmpty(localList)) {
                    //本地事项材料全部id
                    List<String> localAffairMaterialIds = localList.stream().map(e -> e.getId()).collect(Collectors.toList());
                    //请求返回材料全部id
                    List<String> responseAffairMaterialIds = pullAffairMaterialsList.stream().map(e -> e.getID()).collect(Collectors.toList());

                    for (String localAffairMaterialId : localAffairMaterialIds) {
                        if (!responseAffairMaterialIds.contains(localAffairMaterialId)) {
                            log.info("删除本地事项材料，id为【{}】", localAffairMaterialId);
                            affairMaterialsMapper.deleteByPrimaryKey(localAffairMaterialId);
                        }
                    }
                }

                for (PullAffairMaterials pullAffairMaterials : pullAffairMaterialsList) {
                    AffairMaterials assignment = pullAffairMaterials2AffairMaterials(pullAffairMaterials);
                    //新增
                    if (null == affairMaterialsMapper.selectByPrimaryKey(assignment.getId())) {
                        int res = affairMaterialsMapper.insertSelective(assignment);
                        if (res < 1) {
                            throw new BDException("【同步事项材料】数据插入异常");
                        }
                    } else {
                        //是否显示不做修改
                        assignment.setIsmust(null);
                        int res = affairMaterialsMapper.updateByPrimaryKeySelective(assignment);
                        if (res < 1) {
                            throw new BDException("【同步事项材料】数据更新异常");
                        }
                    }
                }
                //删除材料

            } else {
                log.info("【同步事项材料】没有找到相关材料，地址为：{}" + url);
            }


        }
        log.info("【同步事项材料结束】");

    }

    //把同步接口的bean转换为数据bean
    private AffairMaterials pullAffairMaterials2AffairMaterials(PullAffairMaterials pullAffairMaterials) {
        AffairMaterials affairMaterials = AffairMaterials.builder()
                .id(pullAffairMaterials.getID())
                .affairid(pullAffairMaterials.getAFFAIRID() == null ? "" : pullAffairMaterials.getAFFAIRID())
                .tableid(pullAffairMaterials.getTABLEID() == null ? "" : pullAffairMaterials.getTABLEID())
                .examplepath(pullAffairMaterials.getEXAMPLEPATH() == null ? "" : pullAffairMaterials.getEXAMPLEPATH())
                .istop(pullAffairMaterials.getISTOP() == null ? "" : pullAffairMaterials.getISTOP())
//                .imageNum("")
                .matname(pullAffairMaterials.getMATNAME() == null ? "" : pullAffairMaterials.getMATNAME())
                .remarks(pullAffairMaterials.getREMARKS() == null ? "" : pullAffairMaterials.getREMARKS())
                .mattype(pullAffairMaterials.getMATTYPE() == null ? "" : pullAffairMaterials.getMATTYPE())
//                .imageInfo("")
                .emptytablepath(pullAffairMaterials.getEMPTYTABLEPATH() == null ? "" : pullAffairMaterials.getEMPTYTABLEPATH())
                .matgroup(pullAffairMaterials.getMATGROUP() == null ? "" : pullAffairMaterials.getMATGROUP())
                .valid(pullAffairMaterials.getVALID() == null ? "" : pullAffairMaterials.getVALID())
                .materialcode(pullAffairMaterials.getMATERIALCODE() == null ? "" : pullAffairMaterials.getMATERIALCODE())
                .required(pullAffairMaterials.getREQUIRED() == null ? "" : pullAffairMaterials.getREQUIRED())
                .matnumber(pullAffairMaterials.getMATNUMBER() == null ? "" : pullAffairMaterials.getMATNUMBER())
                .ismust(pullAffairMaterials.getISMUST() == null ? "" : pullAffairMaterials.getISMUST())
                .reusetypeid(pullAffairMaterials.getReuseTypeID() == null ? "" : pullAffairMaterials.getReuseTypeID())
                .reusedetail(pullAffairMaterials.getReuseDetail() == null ? "" : pullAffairMaterials.getReuseDetail())
                .matindex(pullAffairMaterials.getMATINDEX() == null ? "" : pullAffairMaterials.getMATINDEX())
                .validdate(pullAffairMaterials.getVALIDDATE() == null ? "" : pullAffairMaterials.getVALIDDATE())
//                .localpath("")
                .build();
        return affairMaterials;

    }

    @Override
    public PageUtils listByAffairId(String affairid, String objindex) {
        List<AffairMaterials> list = affairMaterialsMapper.listByAffairId(affairid);
        List<AffairMaterials> result = new ArrayList<>();
        if(objindex != null && !objindex.equals("")){
            for (AffairMaterials materials:list) {
                String[] matindex = materials.getMatindex().split(",");
                for (String s : matindex) {
                    if (s.equals(objindex)){
                        result.add(materials);
                    }
                }
            }
            return new PageUtils(result, result.size());
        }
        return new PageUtils(list, list.size());
    }
}
