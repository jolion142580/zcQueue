package com.yiko.ss.domain;

import com.yiko.common.domain.BaseDO;

import javax.persistence.*;
import java.util.Date;

@Table(name = "file_info")
public class FileInfoDO extends BaseDO {
    @Id
    private String id;

    private String affairid;

    private String materialid;

    @Column(name = "onlineApplyId")
    private String onlineapplyid;

    private String filename;

    private String localpath;

    private String remark;

    private String openid;

    private String creattime;

    @Column(name = "mediaId")
    private String mediaid;

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
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return affairid
     */
    public String getAffairid() {
        return affairid;
    }

    /**
     * @param affairid
     */
    public void setAffairid(String affairid) {
        this.affairid = affairid == null ? null : affairid.trim();
    }

    /**
     * @return materialid
     */
    public String getMaterialid() {
        return materialid;
    }

    /**
     * @param materialid
     */
    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    /**
     * @return onlineApplyId
     */
    public String getOnlineapplyid() {
        return onlineapplyid;
    }

    /**
     * @param onlineapplyid
     */
    public void setOnlineapplyid(String onlineapplyid) {
        this.onlineapplyid = onlineapplyid == null ? null : onlineapplyid.trim();
    }

    /**
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    /**
     * @return localpath
     */
    public String getLocalpath() {
        return localpath;
    }

    /**
     * @param localpath
     */
    public void setLocalpath(String localpath) {
        this.localpath = localpath == null ? null : localpath.trim();
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * @return creattime
     */
    public String getCreattime() {
        return creattime;
    }

    /**
     * @param creattime
     */
    public void setCreattime(String creattime) {
        this.creattime = creattime == null ? null : creattime.trim();
    }

    /**
     * @return mediaId
     */
    public String getMediaid() {
        return mediaid;
    }

    /**
     * @param mediaid
     */
    public void setMediaid(String mediaid) {
        this.mediaid = mediaid == null ? null : mediaid.trim();
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

}