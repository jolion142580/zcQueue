package com.yiko.ss.service.impl;

import com.google.common.collect.Maps;
import com.yiko.ss.dao.FileInfoDao;
import com.yiko.ss.domain.FileInfoDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileInfoServiceImplTest {
    @Autowired
    FileInfoDao fileInfoDao;

    @Test
    public void list() throws Exception {
        String onlineapplyId = "152904651104913618636";
        String affairId = "148";
        String openId = "oEyt00yz55O7DYPXt6fVGQIjYZmo";
        Map<String, Object> map = Maps.newHashMap();
        map.put("onlineapplyid",onlineapplyId);
        map.put("affairid",affairId);
        List<FileInfoDO> fileInfoDOList = fileInfoDao.list(map);

        Map<String, List<FileInfoDO>> groupBy = fileInfoDOList.stream().collect(Collectors.groupingBy(e -> e.getMaterialid()));
        System.out.println("");
    }

}