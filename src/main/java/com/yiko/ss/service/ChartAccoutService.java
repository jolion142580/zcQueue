package com.yiko.ss.service;

import com.yiko.ss.vo.ChartAccoutVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface ChartAccoutService {
//    ChartAccoutVo queryChartAccountByDate(String startTime, String endTime);

//    ChartAccoutVo noDateSelected();

    ChartAccoutVo allDataByDate(@RequestParam Map<String, Object> params);
}
