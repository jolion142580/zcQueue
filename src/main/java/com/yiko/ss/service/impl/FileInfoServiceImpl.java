package com.yiko.ss.service.impl;

import com.yiko.common.service.impl.BaseServiceImpl;
import com.yiko.common.utils.BDException;
import com.yiko.common.utils.KeyUtil;
import com.yiko.ss.dao.FileInfoDao;
import com.yiko.ss.domain.FileInfoDO;
import com.yiko.ss.service.FileInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FileInfoServiceImpl extends BaseServiceImpl<FileInfoDO> implements FileInfoService {

    @Autowired
    private FileInfoDao fileInfoDao;

    @Override
    protected void setMapping() {
        this.setMapper(this.fileInfoDao, FileInfoDO.class);
    }

//    @Override
//    public PageUtils list(Map<String, Object> params) throws Exception {
//        Query query = new Query(params);
//
//        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
//
//        List<FileInfoDO> fileInfoDOList = fileInfoDao.list(params);
//
//
//        /**
//         * 1、查询出此办事的所有附件集合
//         * 2、按照附件集合 list对 事项材料的id进行分组
//         * 3、每一个事项材料的图片都需要合成一张pdf
//         *
//         */
//        if (CollectionUtils.isNotEmpty(fileInfoDOList) && fileInfoDOList.size() > 1) {
//            //以事项材料id进行分组
//            Map<String, List<FileInfoDO>> mapGroupByMaterialid =
//                    fileInfoDOList.stream().collect(Collectors.groupingBy(e -> e.getMaterialid()));
//            if (null != mapGroupByMaterialid && !mapGroupByMaterialid.isEmpty()) {
//
//                //遍历分组，得到对应事项材料所需的图片
//                for (Map.Entry<String, List<FileInfoDO>> entry : mapGroupByMaterialid.entrySet()) {
//                    List<FileInfoDO> fileInfoDOS = entry.getValue();
//
//                    //所需合并图片的附件集合
//                    List<File> mergeFileList = Lists.newArrayList();
//                    //生成pdf的名称
//                    String targetName = null;
//                    //生成pdf的位置
//                    String targetPath = null;
//                    //如果PdfFileId等于null，则说明没有生成过pdf，如果不为null，需要覆盖
//                    String PdfFileId = null;
//                    String remark = null;
//                    String materialId = null;
//
//                    for (FileInfoDO fileInfoDO : fileInfoDOS) {
//                        if (StringUtils.isBlank(targetName)) {
//                            targetName = fileInfoDO.getRemark() + ".pdf";
//                            targetPath = fileInfoDO.getLocalpath().substring(0, fileInfoDO.getLocalpath().lastIndexOf("/") + 1);
//                            remark = fileInfoDO.getRemark();
//                            materialId = fileInfoDO.getMaterialid();
//                        }
//                        if (fileInfoDO.getFilename().endsWith(".pdf")) {
//                            PdfFileId = fileInfoDO.getId();
//                        }
//                        if (!fileInfoDO.getFilename().endsWith(".pdf") && fileInfoDO.getLocalpath().indexOf("_reduce.jpg") != -1) {
//                            mergeFileList.add(new File(fileInfoDO.getLocalpath()));
//                        }
//
//
//                    }
//                    if (CollectionUtils.isNotEmpty(mergeFileList)) {
//                        File file = new File(targetPath + targetName);
//                        if (file.exists()) {
//                            ImgPdfUtils.imgMerageToPdf(mergeFileList, new File(targetPath + targetName));
//                        }
//                    }
//                    //新增
//                    if (StringUtils.isBlank(PdfFileId)) {
//                        FileInfoDO fileInfoDO = save(remark, (String) params.get("onlineapplyid"), materialId, targetName, targetPath);
//                        fileInfoDOList.add(fileInfoDO);
//                    }
//                    //修改
//                    else {
//                        update(PdfFileId, targetPath + targetName);
//                    }
//
//                }
//            }
//        }
//
//        PageInfo<FileInfoDO> pageInfo = new PageInfo<>(fileInfoDOList);
//        for (FileInfoDO fileInfo : pageInfo.getList()) {
//            fileInfo.setRemark(fileInfo.getRemark() + fileInfo.getFilename().substring(fileInfo.getFilename().lastIndexOf(".")));
//        }
//
//        return new PageUtils(pageInfo.getList(), new Long(pageInfo.getTotal()).intValue() + 1);
//
//    }


    @Override
    public List<FileInfoDO> list(Map<String, Object> map) throws Exception {
        return fileInfoDao.list(map);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    FileInfoDO update(String fileInfoId, String filePath) {
        FileInfoDO fileInfoDO = fileInfoDao.selectByPrimaryKey(fileInfoId);
        if (null == fileInfoDO) {
            throw new BDException("【材料pdf】，更新失败");
        }
        fileInfoDO.setLocalpath(filePath);
        int res = fileInfoDao.updateByPrimaryKeySelective(fileInfoDO);
        if (res < 0) {
            if (res < 0) {
                throw new BDException("【材料pdf】，更新失败");
            }
        }
        return fileInfoDO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    FileInfoDO save(String remark, String onlineApplyId, String materialId, String fileName, String filePath) {
        FileInfoDO fileInfoDO = new FileInfoDO();
        fileInfoDO.setId(KeyUtil.genUniqueKey());
        fileInfoDO.setRemark(remark);
        fileInfoDO.setOnlineapplyid(onlineApplyId);
        fileInfoDO.setMaterialid(materialId);
        fileInfoDO.setFilename(fileName);
        fileInfoDO.setLocalpath(filePath + fileName);
        int res = fileInfoDao.insertSelective(fileInfoDO);
        if (res < 0) {
            throw new BDException("【材料pdf】，保存失败");
        }

        return fileInfoDO;
    }



    public static void main(String[] args) {
        String targe = "c:/wechatfile/fileupload/oEyt00yz55O7DYPXt6fVGQIjYZmo/15290529650181656051745_reduce.jpg";
        String res = targe.substring(0, targe.lastIndexOf("/"));
        System.out.println(res);
    }


}
