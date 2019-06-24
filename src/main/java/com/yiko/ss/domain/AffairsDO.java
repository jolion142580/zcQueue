package com.yiko.ss.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AffairsDO {
    private String affairId;

    private String departId;

    private String affairName;

    private String level;

    private String timeLimitType;

    private String timeLimit;

    private String sortId;

    private String resultForm;

    private String isNet;

    private String code;

    private String isTodo;

    private String isOnline;

    private String isWrite;

    private String config;

    @Transient
    //部门名称
    private String departName;

    @Transient
    //事项主题分类
    private String lifeName;

    private String templateId;

    @Transient
    //判断是否选中该事项
    private String isCheck;


}