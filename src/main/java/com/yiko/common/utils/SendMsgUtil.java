package com.yiko.common.utils;

import com.yiko.common.config.Constant;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMsgUtil {

    public static String send(String msgTxt, String phoneNum) {
        String url = Constant.MESSAGE_URL;
        Map urlMap = new HashMap();
        urlMap.put("method", "sendSMS");
        urlMap.put("content", msgTxt);
        urlMap.put("receiver", phoneNum);
        urlMap.put("token", "0menshi789");
        String content = HttpClientUtil.doPost(url, urlMap);
        JSONObject rs = JSONObject.fromObject(content);
        return rs.getString("result");
    }
}
