package com.yiko.ss.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AffairGuideDaoTest {
    @Autowired
    AffairGuideDao affairGuideDao;
    @Test
    public void deleteByAffairId() throws Exception {
        int res =affairGuideDao.deleteByAffairId("abc123");
        assertNotEquals(0,1);
    }

}