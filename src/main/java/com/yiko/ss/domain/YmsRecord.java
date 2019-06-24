package com.yiko.ss.domain;

import javax.persistence.*;

@Table(name = "yms_record")
public class YmsRecord {

    @Id
    @Column(name = "curr_affaircode")
    private String currAffaircode;

    @Column(name = "audit_opinion")
    private String auditOpinion;

    @Column(name = "statue_desc")
    private String statueDesc;

    private String status;

    @Column(name = "onlineApplyId")
    private String onlineApplyId;

    /**
     * @return curr_affaircode
     */
    public String getCurrAffaircode() {
        return currAffaircode;
    }

    /**
     * @param currAffaircode
     */
    public void setCurrAffaircode(String currAffaircode) {
        this.currAffaircode = currAffaircode == null ? null : currAffaircode.trim();
    }

    /**
     * @return audit_opinion
     */
    public String getAuditOpinion() {
        return auditOpinion;
    }

    /**
     * @param auditOpinion
     */
    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion == null ? null : auditOpinion.trim();
    }

    /**
     * @return statue_desc
     */
    public String getStatueDesc() {
        return statueDesc;
    }

    /**
     * @param statueDesc
     */
    public void setStatueDesc(String statueDesc) {
        this.statueDesc = statueDesc == null ? null : statueDesc.trim();
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getOnlineApplyId() {
        return onlineApplyId;
    }

    public void setOnlineApplyId(String onlineApplyId) {
        this.onlineApplyId = onlineApplyId;
    }
}