package com.yiko.ss.controller;

import com.yiko.common.annotation.Log;
import com.yiko.common.enums.SSEnum;
import com.yiko.common.exception.BootDoException;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.AffairGuideDO;
import com.yiko.ss.service.AffairGuideService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/ss/affairguide")
public class AffairGuideController {
    private final String prefix = "ss/affairguide";
    @Autowired
    AffairGuideService affairGuideService;

    @GetMapping("/view")
    @RequiresPermissions("ss:affairguide:view")
    public String view() {
        return prefix + "/affairguide";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
        return affairGuideService.queryList(params);
    }

    @GetMapping("/add")
    @Log("添加affairguide")
    @RequiresPermissions("ss:affairguide:add")
    public String add() {
        return prefix + "/add";
    }


    @Log("删除Affairguide")
    @RequiresPermissions("ss:affairguide:remove")
    @PostMapping("/remove/{guideId}")
    @ResponseBody
    R remove(@PathVariable("guideId") String guideId) {
        int result = affairGuideService.remove(guideId);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @Log("修改事项对象管理")
    @RequiresPermissions("ss:affairs:edit")
    @GetMapping("/edit/{affairId}")
    String edit(Model model, @PathVariable("affairId") String affairId) {
        AffairGuideDO affairGuideDO = affairGuideService.getAffairGuideByAffairId(affairId);
        if (null == affairGuideDO) {
            throw new BootDoException(SSEnum.FIND_ERROR);
        }
        model.addAttribute("affairguide", affairGuideDO);
        return prefix + "/edit";

    }

    @Log("更新affairguide")
//    @RequiresPermissions("ss:affairguide:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(AffairGuideDO affairGuideDO) {
        int result = affairGuideService.update(affairGuideDO);
        if (result <= 0) {
            return R.error();
        }
        return R.ok();
    }

    @Log("批量删除affairguide")
    @RequiresPermissions("ss:affairguide:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") String[] id) {
        int result = affairGuideService.batchRemove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

}
