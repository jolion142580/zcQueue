package com.yiko.ss.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.ss.service.ChartAccoutService;
import com.yiko.ss.service.OnlineApplyService;
import com.yiko.ss.service.UserInfoService;
import com.yiko.ss.service.YmsRecordService;
import com.yiko.ss.vo.ChartAccoutVo;
import com.yiko.ss.vo.OnlineApplyCountVo;
import com.yiko.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/chart")
public class ChartAccoutController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserService userService;

    @Autowired
    OnlineApplyService onlineApplyService;

    @Autowired
    YmsRecordService ymsRecordService;

    @Autowired
    ChartAccoutService chartAccoutService;

    private String prefix = "ss/chartAccout";

    @RequestMapping("/view")
    public String view(Model model) {
        return prefix + "/chartAcount";
    }

    @RequestMapping("/queryList")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
        List<ChartAccoutVo> chartAccoutVos = Lists.newArrayList();
        chartAccoutVos.add(chartAccoutService.allDataByDate(params));
        PageInfo<ChartAccoutVo> pageInfo = new PageInfo<>(chartAccoutVos);
        return new PageUtils(pageInfo.getList(), new Long(pageInfo.getTotal()).intValue());

    }

    //柱状图请求
    @RequestMapping("/calculateDateTotal")
    @ResponseBody
    public R onlineDataLimit(@RequestParam Map<String, Object> params) {
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        List<OnlineApplyCountVo> onlineApplyCountVos = onlineApplyService.calculateDateTotal(startTime, endTime);
        return R.ok().put("data", onlineApplyCountVos);
    }




}
