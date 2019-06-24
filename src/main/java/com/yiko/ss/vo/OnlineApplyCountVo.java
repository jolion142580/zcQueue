package com.yiko.ss.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineApplyCountVo {

    private Integer affairCount = 0;

    private String affairName = "";

}
