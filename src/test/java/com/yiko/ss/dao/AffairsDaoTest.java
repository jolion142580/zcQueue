package com.yiko.ss.dao;

import com.yiko.ss.domain.AffairsDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AffairsDaoTest {
    @Autowired
    AffairsDao affairsDao;

    @Test
    public void insertSelective() throws Exception {
        AffairsDO affairsDO = AffairsDO.builder()
                .affairId("123").affairName("abc").build();
        affairsDao.insertSelective(affairsDO);
    }

    @Test
    public void delete() throws Exception {

       int res= affairsDao.remove("fldskjflksdjflkdsjalfkj3284928371312");
        System.out.println("============================"+res);
    }

}