package com.yiko.ss.domain;

import javax.persistence.*;

@Table(name = "pretrialOpinion")
public class PretrialOpinion {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    @Column(name = "opinionIndex")
    private String opinionindex;

    @Column(name = "opinionText")
    private String opiniontext;

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
     * @return opinionIndex
     */
    public String getOpinionindex() {
        return opinionindex;
    }

    /**
     * @param opinionindex
     */
    public void setOpinionindex(String opinionindex) {
        this.opinionindex = opinionindex == null ? null : opinionindex.trim();
    }

    /**
     * @return opinionText
     */
    public String getOpiniontext() {
        return opiniontext;
    }

    /**
     * @param opiniontext
     */
    public void setOpiniontext(String opiniontext) {
        this.opiniontext = opiniontext == null ? null : opiniontext.trim();
    }
}