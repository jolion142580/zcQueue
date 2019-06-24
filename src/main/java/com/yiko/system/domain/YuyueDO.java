package com.yiko.system.domain;

import com.yiko.common.domain.BaseDO;

import javax.persistence.*;
import java.util.Date;

@Table(name = "yuyues")
public class YuyueDO extends BaseDO {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private String name;

    private String idcard;

    private String phone;

    private String no;

    private String street;

    private String state;

    private String stype;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCreator() {
        return null;
    }

    @Override
    public void setCreator(String creator) {

    }

    @Override
    public Date getCreateTime() {
        return null;
    }

    @Override
    public void setCreateTime(Date date) {

    }

    @Override
    public String getUpdater() {
        return null;
    }

    @Override
    public void setUpdater(String updater) {

    }

    @Override
    public Date getUpdateTime() {
        return null;
    }

    @Override
    public void setUpdateTime(Date date) {

    }

    @Override
    public String getDataState() {
        return null;
    }

    @Override
    public void setDataState(String datestate) {

    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return idcard
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * @param idcard
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return no
     */
    public String getNo() {
        return no;
    }

    /**
     * @param no
     */
    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    /**
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street
     */
    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * @return stype
     */
    public String getStype() {
        return stype;
    }

    /**
     * @param stype
     */
    public void setStype(String stype) {
        this.stype = stype == null ? null : stype.trim();
    }
}