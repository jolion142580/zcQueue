package com.yiko.ss.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AffairObjectMapperTest {
    @Autowired
    AffairObjectMapper affairObjectMapper;
    @Test
    public void deleteByAffairId() throws Exception {
       int res = affairObjectMapper.deleteByAffairId("abc123");
//        Assert.assertNotEquals(0,1);
        System.out.println(res);
    }

}