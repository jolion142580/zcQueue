package com.yiko.ss.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffairObject {
    private String objid;

    private String objindex;

    private String objname;

    private String affairid;

    private String isonline;

    private String iswrite;

    private String config;

    private String templateid;

    private String config1;

    private String templateid1;

    private String isshow;


}