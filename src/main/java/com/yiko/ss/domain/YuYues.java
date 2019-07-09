package com.yiko.ss.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YuYues {
    private int id;
    private String name;
    private String idcard;
    private String phone;
    private String no;
    private String street;
    private int state;// 0：未处理；1：已处理；2：已取消；3：补签；4：失约。
    private String stype;
    private String terminal;
    private String ystime;
    private String yetime;
    private String ydate;
    private Date cdate;
    private Date qdate;
    private String timecode;
    private String openid;
    private int weight;
    private String businessName;
    private String appraiseResult;
    private String appraiseAdivce;
    private String queueNum;
    private String tdstart;
    private String fwstart;
    private String glstart;
    private String allstart;
    private String USING;
    private String UseAdvice;
    private String OtherAdvice;
    private String cSource;
    private String operator; //执行人

}
