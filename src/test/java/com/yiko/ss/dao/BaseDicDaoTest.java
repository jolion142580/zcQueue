package com.yiko.ss.dao;

import com.yiko.common.utils.KeyUtil;
import com.yiko.ss.domain.BaseDicDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseDicDaoTest {
    @Autowired
    BaseDicDao baseDicDao;
    @Test
    public void insertSelective() throws Exception {
        BaseDicDO baseDicDO =new BaseDicDO();
        baseDicDO.setId(KeyUtil.genUniqueKey());
        baseDicDO.setcName("test");
        baseDicDO.setBaseDicId("123");
        baseDicDO.setBaseDicType("depart");
        baseDicDao.insertSelective(baseDicDO);
    }

}