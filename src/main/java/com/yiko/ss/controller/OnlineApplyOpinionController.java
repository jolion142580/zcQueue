package com.yiko.ss.controller;

import com.yiko.common.utils.R;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.service.OnlineApplyOpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ss/onlineApplyOpinion")
public class OnlineApplyOpinionController {
    @Autowired
    OnlineApplyOpinionService onlineApplyOpinionService;

    @ResponseBody
    @RequestMapping("/add")
    R addOpinion(OnlineApplyDO onlineApplyDO) {

        return onlineApplyOpinionService.addOpinion(onlineApplyDO);
    }
}
