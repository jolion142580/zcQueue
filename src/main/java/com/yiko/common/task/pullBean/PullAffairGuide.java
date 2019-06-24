package com.yiko.common.task.pullBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 同步办事指南所需bean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullAffairGuide {

    //办事指南ID
    private String GUIDEID;

    //事项ID
    private String AFFAIRID;

    //政策依据
    private String ACCORDING;

    //办事流程
    private String PROCEDURES;

    //证明材料
    private String MATERIAL;

    //所需条件
    private String INSTITUTION;

    //附件路径
    private String ACCESSORYPATH;

    //受理条件
    private String CONDITION;

    //办理部门
    private String XRNDOMU;

    //受理地址
    private String SITE;

    //办结时限
    private String TIME;

    //咨询查询
    private String INQUIRE;

    //收费标准
    private String CHARGE;

    //收费依据
    private String CHARGEGIST;

    //特别说明
    private String SEPCIALVERSION;
}
