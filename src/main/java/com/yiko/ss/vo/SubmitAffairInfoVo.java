package com.yiko.ss.vo;

import java.util.List;

public class SubmitAffairInfoVo {

    private String affairid;

    private String towncode = "3";

    private String attribution_id;

    private String username;

    private String idcard;

    private Integer sex;

    private String birthday;

    private String phone;

    private String mobilephone;

    private String residence_address;

    private String live_address;

    private String is_legalperson = "0";

    private String objid;

    private String remark;

    private List<MakeInfo> makeinfos;

    public String getAttribution_id() {
        return attribution_id;
    }

    public void setAttribution_id(String attribution_id) {
        this.attribution_id = attribution_id;
    }

    public String getAffairid() {
        return affairid;
    }

    public void setAffairid(String affairid) {
        this.affairid = affairid;
    }

    public String getTowncode() {
        return towncode;
    }

    public void setTowncode(String towncode) {
        this.towncode = towncode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getResidence_address() {
        return residence_address;
    }

    public void setResidence_address(String residence_address) {
        this.residence_address = residence_address;
    }

    public String getLive_address() {
        return live_address;
    }

    public void setLive_address(String live_address) {
        this.live_address = live_address;
    }

    public String getIs_legalperson() {
        return is_legalperson;
    }

    public void setIs_legalperson(String is_legalperson) {
        this.is_legalperson = is_legalperson;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<MakeInfo> getMakeinfos() {
        return makeinfos;
    }

    public void setMakeinfos(List<MakeInfo> makeinfos) {
        this.makeinfos = makeinfos;
    }

}
