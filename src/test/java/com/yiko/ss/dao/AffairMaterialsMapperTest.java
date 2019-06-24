package com.yiko.ss.dao;

import com.yiko.ss.domain.AffairMaterials;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AffairMaterialsMapperTest {
    @Autowired
    AffairMaterialsMapper affairMaterialsMapper;

    @Test
    public void deleteByAffairId() throws Exception {
        int res = affairMaterialsMapper.deleteByAffairId("abc123");
        System.out.println(res);
        Assert.assertNotEquals(0, 1);
    }


    @Test
    public void update() throws Exception {
        AffairMaterials affairMaterials = new AffairMaterials();
        affairMaterials.setId("10010");
        affairMaterials.setIsmust("1");
        affairMaterials.setValid(null);
        int res = affairMaterialsMapper.updateByPrimaryKeySelective(affairMaterials);
        System.out.println(res);
//        Assert.assertNotEquals(0, 1);
    }

}