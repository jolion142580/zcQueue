package com.yiko.ss.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffairMaterials {
    private String id;

    private String affairid;

    private String tableid;

    private String examplepath;

    private String istop;

    private String imageNum;

    private String matname;

    private String remarks;

    private String mattype;

    private String imageInfo;

    private String emptytablepath;

    private String matgroup;

    private String valid;

    private String materialcode;

    private String required;

    private String matnumber;

    private String ismust;

    private String reusetypeid;

    private String reusedetail;

    private String matindex;

    private String validdate;

    private String localpath;

}