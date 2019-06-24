package com.yiko.ss.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 图表所需参数
 */
public class ChartAccoutVo {

    //注册总人数
    private Integer registerUserCount;

    //完结事项总数
    private Integer handlingMattersCount;

    //提交事项人数
    private Integer commitAffairCount;


}
