package com.yiko.ss.domain;

public class Complaint {
    private String cId;

    private String complaintreplytime;

    private String complainttime;

    private String complaintContent;

    private String complaintName;

    private String complaintNum;

    private String complaintPho;

    private String complaintRemark;

    private String complaintReply;

    private String complaintShow;

    private String complaintStatus;

    private String complaintTitle;

    private String openId;


    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId == null ? null : cId.trim();
    }

    public String getComplaintreplytime() {
        return complaintreplytime;
    }

    public void setComplaintreplytime(String complaintreplytime) {
        this.complaintreplytime = complaintreplytime == null ? null : complaintreplytime.trim();
    }

    public String getComplainttime() {
        return complainttime;
    }

    public void setComplainttime(String complainttime) {
        this.complainttime = complainttime == null ? null : complainttime.trim();
    }

    public String getComplaintContent() {
        return complaintContent;
    }

    public void setComplaintContent(String complaintContent) {
        this.complaintContent = complaintContent == null ? null : complaintContent.trim();
    }

    public String getComplaintName() {
        return complaintName;
    }

    public void setComplaintName(String complaintName) {
        this.complaintName = complaintName == null ? null : complaintName.trim();
    }

    public String getComplaintNum() {
        return complaintNum;
    }

    public void setComplaintNum(String complaintNum) {
        this.complaintNum = complaintNum == null ? null : complaintNum.trim();
    }

    public String getComplaintPho() {
        return complaintPho;
    }

    public void setComplaintPho(String complaintPho) {
        this.complaintPho = complaintPho == null ? null : complaintPho.trim();
    }

    public String getComplaintRemark() {
        return complaintRemark;
    }

    public void setComplaintRemark(String complaintRemark) {
        this.complaintRemark = complaintRemark == null ? null : complaintRemark.trim();
    }

    public String getComplaintReply() {
        return complaintReply;
    }

    public void setComplaintReply(String complaintReply) {
        this.complaintReply = complaintReply == null ? null : complaintReply.trim();
    }

    public String getComplaintShow() {
        return complaintShow;
    }

    public void setComplaintShow(String complaintShow) {
        this.complaintShow = complaintShow == null ? null : complaintShow.trim();
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus == null ? null : complaintStatus.trim();
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle == null ? null : complaintTitle.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }
}