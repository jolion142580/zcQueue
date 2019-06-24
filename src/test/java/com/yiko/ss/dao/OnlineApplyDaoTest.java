package com.yiko.ss.dao;

import com.yiko.ss.domain.OnlineApplyDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnlineApplyDaoTest {
    @Autowired
    OnlineApplyDao onlineApplyDao;
    @Test
    public void listUserNameAndAffairNameByOnlineId() throws Exception {
      OnlineApplyDO onlineApplyDO = onlineApplyDao.queryUserNameAndAffairNameByOnlineId("153421246621422850764");
        System.out.println("");
    }

}