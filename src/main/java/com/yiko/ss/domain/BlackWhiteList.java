package com.yiko.ss.domain;

import com.yiko.common.domain.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//@Table(name="black_white_list")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackWhiteList extends BaseDO {

    private String id;
    private String openid;
    private String name;
    private String idCard;
    private String phone;
    private String time;
    private String flag;
    private String forever;
    private String creator;
    private String creattime;
    private String updater;
    private String updatetime;

    //BaseDO 补充
    private Date CreateTime;
    private Date updateTime;
    private String dataState;


    @Override
    public Date getCreateTime() {
        return CreateTime;
    }
    @Override
    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }
    @Override
    public Date getUpdateTime() {
        return updateTime;
    }
    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public String getDataState() {
        return dataState;
    }
    @Override
    public void setDataState(String dataState) {
        this.dataState = dataState;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String getCreator() {
        return creator;
    }
    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getCreattime() {
        return creattime;
    }
    public void setCreattime(String creattime) {
        this.creattime = creattime;
    }
    @Override
    public String getUpdater() {
        return updater;
    }
    @Override
    public void setUpdater(String updater) {
        this.updater = updater;
    }
    public String getUpdatetime() {
        return updatetime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getForever() {
        return forever;
    }

    public void setForever(String forever) {
        this.forever = forever;
    }
}
