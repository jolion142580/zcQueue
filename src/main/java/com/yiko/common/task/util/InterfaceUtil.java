package com.yiko.common.task.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yiko.common.task.pullBean.PullAffairObject;
import com.yiko.common.utils.GsonUtil;
import com.yiko.common.utils.HttpClientUtil;
import com.yiko.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InterfaceUtil {

    //把son字符串的data属性转换为list中的对象
    public static <T> List<T> parseJsonToBeanByData(Class<T> cls, String interfaceUrl) {
        log.info("=================开始转换，传入类名：{}，接口地址：{}==================", cls.getName(), interfaceUrl);
        String strJson = HttpClientUtil.doGet(interfaceUrl);
//      String strJson = readToString("D:\\affairObject.txt");
        JsonObject jsonObject = new JsonParser().parse(strJson).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        ArrayList<T> beans = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            T tdata = GsonUtil.GsonToBean(jsonElement, cls);
            beans.add(tdata);
        }
        log.info("===============转换结束，list长度为：{}===================", beans.size());
        return beans;

    }

    //把son字符串中的data属性转换为对象
    public static <T> T parseJsonToBeanByDataSimple(Class<T> cls, String interfaceUrl) {
        log.info("=================开始转换，传入类名：{}，接口地址：{}==================", cls.getName(), interfaceUrl);
        String strJson = HttpClientUtil.doGet(interfaceUrl);
//        String strJson = readToString("D:\\affairObject.txt");
        if (StringUtils.isNotBlank(strJson)) {
            JsonObject jsonObject = new JsonParser().parse(strJson).getAsJsonObject();
            JsonElement jsonElement = jsonObject.get("data");
            T t = GsonUtil.GsonToBean(jsonElement, cls);
            return t;
        } else {
            return null;
        }

    }


    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
//        PullAffairGuide pullAffairGuide = parseJsonToBeanByDataSimple(PullAffairGuide.class);
//        List<PullAffairs> list = parseJsonToBeanByData(PullAffairs.class, "");
//        PullAffairObject pullAffairObject =parseJsonToBeanByDataSimple(PullAffairObject.class,"");
        List<PullAffairObject> pullAffairObjectList = parseJsonToBeanByData(PullAffairObject.class, "");
        System.out.println("");


    }
}
