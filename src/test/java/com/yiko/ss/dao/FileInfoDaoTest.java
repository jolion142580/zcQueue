package com.yiko.ss.dao;

import com.yiko.common.exception.BDException;
import com.yiko.ss.domain.FileInfoDO;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileInfoDaoTest {
    @Autowired
    FileInfoDao fileInfoDao;

    @Test
    public void list() throws Exception {
    }


    @Test
    public void update() throws Exception {
        List<FileInfoDO> fileInfoDOS = fileInfoDao.selectAll();
        for (FileInfoDO fileInfoDO : fileInfoDOS) {
            if (StringUtils.isNotBlank(fileInfoDO.getLocalpath())) {
                String prefix = fileInfoDO.getLocalpath().substring(0, 1);
                if (prefix.equalsIgnoreCase("c")) {
                    String replPath = fileInfoDO.getLocalpath().replaceFirst(prefix, "d");
                    FileInfoDO repFileInfo = new FileInfoDO();
                    repFileInfo.setId(fileInfoDO.getId());
                    repFileInfo.setLocalpath(replPath);
                    int res = fileInfoDao.updateByPrimaryKeySelective(fileInfoDO);
                    if (res < 1) {
                        throw new BDException("更新失败");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "c:/wechatfile/fileupload/ccc-wi7OE5b8qGXsG8xc/ccc/15324051459801205459168_reduce.jpg";

        if (StringUtils.isNotBlank(filePath)) {
            String prefix = filePath.substring(0, 1);
            if (prefix.equalsIgnoreCase("c")) {
                filePath = filePath.replaceFirst(prefix, "D");
            }
        }
        System.out.println(filePath);
    }

}