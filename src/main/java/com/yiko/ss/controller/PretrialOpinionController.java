package com.yiko.ss.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.AffairsDO;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.domain.PretrialOpinion;
import com.yiko.ss.domain.UserInfo;
import com.yiko.ss.service.AffairsService;
import com.yiko.ss.service.OnlineApplyService;
import com.yiko.ss.service.PretrialOpinionService;
import com.yiko.ss.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/pretrialopinion")
public class PretrialOpinionController {
    private static final String prefix = "ss/onlineApply";

    @Autowired
    PretrialOpinionService pretrialOpinionService;

    @Autowired
    OnlineApplyService onlineApplyService;

    @Autowired
    AffairsService affairsService;

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<PretrialOpinion> pretrialOpinionList = pretrialOpinionService.selectByConditions(params);
        PageInfo<PretrialOpinion> pageInfo = new PageInfo<>(pretrialOpinionList);
        return new PageUtils(pretrialOpinionList, new Long(pageInfo.getTotal()).intValue());
    }

    @GetMapping(value = "/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        String[] data = id.split(":");
        model.addAttribute("onlineapplyid", data[0]);
        model.addAttribute("event", data[2]);
        model.addAttribute("opinionIndex", data[1]);
        return prefix + "/pretrialopinion";
    }

//    @PostMapping("/submit")
//    @ResponseBody
//    R submit(@RequestParam("id") String id, @RequestParam("onlineApplyId") String onlineApplyId, @RequestParam("event") String event) {
//        PretrialOpinion pretrialOpinion = pretrialOpinionService.selectByKey(id);
//        String text = pretrialOpinion.getOpiniontext();
//        OnlineApplyDO onlineApplyDO = onlineApplyService.selectByKey(onlineApplyId);
//        AffairsDO affairsDO = affairsService.get(onlineApplyDO.getAffairid());
//        String affairName = affairsDO.getAffairName();
//        text = text.replaceAll("\\*\\*\\*\\*\\*", affairName);
//        Map map = new HashMap();
//        map.put("id", onlineApplyDO.getOpenid());
//        UserInfo userInfo = userInfoService.getById(map);
//        text = text.replaceAll("XX", userInfo.getName());
//
//        Map<String, Object> rMap = new HashMap<>();
//
//        if (event.equals("opinionbutton")) {
//            onlineApplyDO.setDepartopinion(text);
//            rMap.put("buttonId", "opinionbutton");
//        } else if (event.equals("opinionfailure")) {
//            onlineApplyDO.setOpinion(text);
//            rMap.put("buttonId", "opinionfailure");
//        }
//        else if (event.equals("opinionidea")) {
//            onlineApplyDO.setRemark(text);
//            rMap.put("buttonId", "opinionidea");
//        }
//
//        int result = onlineApplyService.updateNotNull(onlineApplyDO);
//
//        if (result > 0) {
//            rMap.put("onlineApplyDO", onlineApplyDO);
//            return R.ok(rMap);
//        }
//        return R.error();
//    }

    @PostMapping("/submit")
    @ResponseBody
    R submit(@RequestParam("id") String id, @RequestParam("onlineApplyId") String onlineApplyId, @RequestParam("event") String event) {
        PretrialOpinion pretrialOpinion = pretrialOpinionService.selectByKey(id);
        String text = pretrialOpinion.getOpiniontext();
        OnlineApplyDO onlineApplyDO = onlineApplyService.selectByKey(onlineApplyId);
        AffairsDO affairsDO = affairsService.get(onlineApplyDO.getAffairid());
        String affairName = affairsDO.getAffairName();
        text = text.replaceAll("\\*\\*\\*\\*\\*", affairName);
        Map map = new HashMap();
        map.put("id", onlineApplyDO.getOpenid());
        UserInfo userInfo = userInfoService.getById(map);
        text = text.replaceAll("XX", userInfo.getName());

        Map<String, Object> rMap = new HashMap<>();

        //审核意见 ->不通过

        if (event.equals("opinionfailure")) {
            rMap.put("buttonId", "opinionfailure");
        }
        ////部门意见模板
        else if (event.equals("opinionbutton")) {
            rMap.put("buttonId", "opinionbutton");
        }
        //审核意见模板
        else if (event.equals("opinionidea")) {
            onlineApplyDO.setOpinion(text);
            rMap.put("buttonId", "opinionidea");
        }

        rMap.put("context", text);
        return R.ok(rMap);
    }
}
