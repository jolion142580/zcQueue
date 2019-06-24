package com.yiko.ss.dao;

import com.yiko.ss.domain.OnlineApplyOpinion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OnlineApplyOpinionMapperTest {
    @Autowired
    OnlineApplyOpinionMapper onlineApplyOpinionMapper;

    @Test
    public void test() {
        Example example = new Example(OnlineApplyOpinion.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("onlineapplyid", "21398172398712");
        int res = onlineApplyOpinionMapper.selectCountByExample(example);
    }

}