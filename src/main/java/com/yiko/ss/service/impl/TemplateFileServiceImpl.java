package com.yiko.ss.service.impl;

import com.google.common.collect.Maps;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.domain.Tree;
import com.yiko.common.utils.*;
import com.yiko.ss.dao.TemplateFileMapper;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.domain.FileInfoDO;
import com.yiko.ss.domain.TemplateFile;
import com.yiko.ss.service.AffairsService;
import com.yiko.ss.service.BaseDicService;
import com.yiko.ss.service.TemplateFileService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemplateFileServiceImpl implements TemplateFileService {

    //@Autowired
    //WebSocket webSocket;


    @Autowired
    BaseDicService baseDicService;

    @Autowired
    TemplateFileMapper templateFileMapper;

    @Autowired
    BootdoConfig bootdoConfig;

    @Autowired
    AffairsService affairsService;

    @Override
    public Tree<BaseDicDO> getTree() {
        List<Tree<BaseDicDO>> trees = new ArrayList<>();
        Map baseMap = new HashMap();
        baseMap.put("baseDicType", "depart");
        List<BaseDicDO> baseDicDOList = baseDicService.queryList(baseMap);
        for (BaseDicDO baseDicDO : baseDicDOList) {
            Tree<BaseDicDO> tree = new Tree<>();
            tree.setId(baseDicDO.getBaseDicId());
            tree.setText(baseDicDO.getcName());
            trees.add(tree);
        }
        return BuildTree.build(trees);
    }

    @Override
    public List<TemplateFile> list(Map<String, Object> map) {
        return templateFileMapper.list(map);
    }

    @Override
    public TemplateFile get(Map<String, Object> map) {
        return templateFileMapper.get(map);
    }

    @Override
    public int save(TemplateFile templateFile) {
        return templateFileMapper.insertSelective(templateFile);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R saveOrUpdate(String templatefile, MultipartFile file) {
        JSONObject jsonObject = JSONObject.fromObject(templatefile);
        TemplateFile templateFile = (TemplateFile) JSONObject.toBean(jsonObject, TemplateFile.class);
        //保存的文件名
        String saveFileName = null;
        //保存的地址
        String uploadPath = null;
        String date = TimeUtils.getStringDate();

        if (StringUtils.isNotBlank(templateFile.getId())) {
            templateFile.setDepartid(templateFileMapper.selectByPrimaryKey(templateFile.getId()).getDepartid());
        }

        if (null != file) {
            String fileName = file.getOriginalFilename();
            boolean flag = FileUtil.isSuccessFileName(fileName);
            if (!flag) {
                return R.error("只能上传.doc  .docx  .xls  类型的文件!");
            }
            String prefixDate = date.substring(0, 4);
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            saveFileName = fileName.substring(0, fileName.lastIndexOf(".")) + KeyUtil.genUniqueKey();
            uploadPath = bootdoConfig.getTemplatefile() + prefixDate + "\\" + templateFile.getDepartid() + "\\";
            //文件后缀
            uploadPath = uploadPath.replaceAll("/", "\\\\");
            templateFile.setFilename(saveFileName);
            templateFile.setLocalpath(uploadPath + saveFileName + suffix);

            try {
                FileUtil.uploadFile(file.getBytes(), uploadPath, saveFileName + suffix);
            } catch (Exception e) {
                return R.error();
            }
        }


        //更新
        if (StringUtils.isNotBlank(templateFile.getId())) {
            int result = templateFileMapper.update(templateFile);
            if (result < 0) {
                return R.error("模板更新失败");
            }
        }
        //插入
        else {
            String id = KeyUtil.genUniqueKey();
            templateFile.setCreatetime(date);
            templateFile.setId(id);
            int result = templateFileMapper.insertSelective(templateFile);
            if (result < 0) {
                return R.error("模板插入失败");
            }

        }
        return R.ok();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(String id) {
        TemplateFile templateFile = templateFileMapper.selectByPrimaryKey(id);
        //删除模板后删除文件
        String filePath = templateFile.getLocalpath();
        FileUtil.deleteFile(filePath);
        int templateRes = templateFileMapper.remove(id);

        int affairsRes = affairsService.updateByTemplateId(id);
        if (templateRes < 0 || affairsRes < 0) {
            throw new BDException("删除模板失败");
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int batchRemove(String[] ids) {
        return templateFileMapper.batchRemove(ids);
    }

}
