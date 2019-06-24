package com.yiko.common.utils;

import com.alibaba.fastjson.JSON;
import com.yiko.ss.domain.AffairGuideDO;
import com.yiko.ss.domain.AffairsDO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InterfaceInfo {
    //@Value("${interfaceUrl}")
    private String url;

    public  List<AffairsDO> getAffairs(String isnet){
        url+="getAffairs";
        Map map =new HashMap();
        map.put("isnet",isnet);
        String data = HttpClientUtil.doPost(url,map);
        List<AffairsDO> affairsList= JSON.parseArray(data,AffairsDO.class);
        return affairsList;
    }

   public  List<AffairGuideDO> getAffairGuide(String affair_id){
       url+="getAffairGuide";
       Map map =new HashMap();
       map.put("affair_id",affair_id);
       String data = HttpClientUtil.doPost(url,map);
       List<AffairGuideDO> affairGuideList= JSON.parseArray(data,AffairGuideDO.class);
       return affairGuideList;
   }
}
