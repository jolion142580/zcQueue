package com.yiko.ss.service;

import com.yiko.common.domain.Tree;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.AffairMaterials;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AffairMaterialsService {

    PageUtils queryList(Map<String,Object> map);

    R save(String affairMaterials,String objId, MultipartFile file);

    AffairMaterials selectOneByCondition(Map<String,Object> map);

    int remove(String id);

    int batchRemove(String []id);

    R update( String affairMaterials,MultipartFile file);


    Tree<Object> getTreeAndAffairType();

    void pullAffairMaterials();

    int removePath(String id);

    PageUtils listByAffairId(String affairid, String objindex);
}
