package com.yiko.ss.dao;

import com.yiko.ss.domain.AffairMaterials;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AffairMaterialsMapper {
    int deleteByPrimaryKey(String id);

    AffairMaterials selectByPrimaryKey(String affairMaterialsId);

    int deleteByAffairId(String affairId);

    int insert(AffairMaterials record);

    int insertSelective(AffairMaterials record);

    AffairMaterials selectOne(Map<String, Object> map);

    int updateByPrimaryKeySelective(AffairMaterials record);

    int updateByPrimaryKey(AffairMaterials record);

    //根据事项对象索引和事项id查询实现材料
    List<AffairMaterials> selectAffairMaterials(@Param("affairId") String affairId,@Param("objIndex") String objIndex);

    int batchRemove(String[] id);

    int SetLocalPathByNull(String affairMateriaId);

    List<AffairMaterials> listByAffairId(String affairid);
}