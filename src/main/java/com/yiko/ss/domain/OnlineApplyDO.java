package com.yiko.ss.domain;

import com.yiko.common.domain.BaseDO;

import javax.persistence.*;
import java.util.Date;

@Table(name = "onlineApply")

public class OnlineApplyDO extends BaseDO {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private String affairid;

    private String openid;

    private String onlinedata;

    private String onlinedata1;

    private String iscommit;

    private String state;

    private String limitdate;


    private String opinion;


    private String creattime;


    private String objindex;


    @Column(name = "curr_affaircode")
    private String currAffaircode;

    @Column(name = "statue_desc")
    private String statueDesc;

    //    private String remark;


    //    private String depart;

    //    private String departopinion;

//    //下一个审核人id
//    @Column(name = "userId")
//    private String userId;
//
//    //第一个审核人id
//    @Column(name = "firstUserId")
//    private String firstUserId;

    //是否推送过办理信息给用户  1->已推送   0||null->未推送过
    @Column(name = "push_flag")
    private String pushFlag;


    @Transient
    private String objname;

    //事项名
    @Transient
    private String affairname;

    @Transient
    //用户名
    private String name;

    //审核通过标记   1、通过  0、失败
    @Column(name = "approved_or_not")
    private String approvedOrNot;

    //审核人名称
    @Column(name = "handle_name")
    private String handleName;

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getApprovedOrNot() {
        return approvedOrNot;
    }

    public void setApprovedOrNot(String approvedOrNot) {
        this.approvedOrNot = approvedOrNot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAffairid() {
        return affairid;
    }

    public void setAffairid(String affairid) {
        this.affairid = affairid == null ? null : affairid.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getOnlinedata() {
        return onlinedata;
    }

    public void setOnlinedata(String onlinedata) {
        this.onlinedata = onlinedata == null ? null : onlinedata.trim();
    }

    public String getIscommit() {
        return iscommit;
    }

    public void setIscommit(String iscommit) {
        this.iscommit = iscommit == null ? null : iscommit.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getLimitdate() {
        return limitdate;
    }

    public void setLimitdate(String limitdate) {
        this.limitdate = limitdate == null ? null : limitdate.trim();
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }


    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime == null ? null : creattime.trim();
    }

    public String getCurrAffaircode() {
        return currAffaircode;
    }

    public void setCurrAffaircode(String currAffaircode) {
        this.currAffaircode = currAffaircode;
    }

    @Override
    public Date getUpdateTime() {
        return null;
    }

    @Override
    public void setUpdateTime(Date date) {

    }

    @Override
    public Date getCreateTime() {
        return null;
    }

    @Override
    public void setCreateTime(Date date) {

    }

    @Override
    public String getCreator() {
        return null;
    }

    @Override
    public void setCreator(String creator) {

    }

    @Override
    public String getDataState() {
        return null;
    }

    @Override
    public void setDataState(String datestate) {

    }

    @Override
    public String getUpdater() {
        return null;
    }

    @Override
    public void setUpdater(String updater) {

    }

    public String getAffairname() {
        return affairname;
    }

    public void setAffairname(String affairname) {
        this.affairname = affairname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getObjindex() {
        return objindex;
    }

    public void setObjindex(String objindex) {
        this.objindex = objindex;
    }

    public String getObjname() {
        return objname;
    }

    public void setObjname(String objname) {
        this.objname = objname;
    }

    public String getStatueDesc() {
        return statueDesc;
    }

    public void setStatueDesc(String statueDesc) {
        this.statueDesc = statueDesc;
    }

    public String getOnlinedata1() {
        return onlinedata1;
    }


    public void setOnlinedata1(String onlinedata1) {


        this.onlinedata1 = onlinedata1;
    }

    public String getPushFlag() {
        return pushFlag;
    }

    public void setPushFlag(String pushFlag) {
        this.pushFlag = pushFlag;
    }
}
