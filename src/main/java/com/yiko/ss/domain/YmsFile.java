package com.yiko.ss.domain;

import javax.persistence.*;

@Table(name = "yms_file")
public class YmsFile {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    @Column(name = "curr_affaircode")
    private String currAffaircode;

    @Column(name = "file_id")
    private String fileId;

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
     * @return file_id
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * @param fileId
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }
}