package com.yiko.ss.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffairGuideDO {
    private String guideId;

    private String affairId;

    private String accessoryPath;

    private String according;

    private String charge;

    private String chargegist;

    private String condition;

    private String inquire;

    private String institution;

    private String material;

    private String procedures;

    private String sepcialversion;

    private String site;

    private String time;

    private String xrndomu;


}