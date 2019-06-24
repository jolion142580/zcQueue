package com.yiko.ss.controller;

import com.sun.crypto.provider.DESParameters;
import com.yiko.common.annotation.Log;
import com.yiko.common.domain.BaseDO;
import com.yiko.common.domain.Tree;
import com.yiko.common.enums.SSEnum;
import com.yiko.common.exception.BootDoException;
import com.yiko.common.utils.KeyUtil;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.AffairGuideDO;
import com.yiko.ss.domain.AffairsDO;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.domain.TemplateFile;
import com.yiko.ss.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/affairs")
public class AffairsController {
    private String prefix = "ss/affairs";
    @Autowired
    AffairsService affairsService;

    @Autowired
    BaseDicService baseDicService;

    @Autowired
    TemplateFileService templateFileService;
    @Autowired
    AffairObjectService affairObjectService;

    @Autowired
    AffairGuideService affairGuideService;

    @GetMapping(value = "/view")
    @RequiresPermissions("ss:affairs:view")
    public String view() {
        return prefix + "/affairs";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
        return affairsService.queryList(params);
    }

    @GetMapping("/add")
    @Log("添加事项管理")
    @RequiresPermissions("ss:affairs:add")
    public String add(Model model) {
        Map<String, Object> map = affairsService.showAddAffairPage();
        model.addAttribute("departList", map.get("departList"));
        model.addAttribute("lifeList", map.get("lifeList"));
        return prefix + "/add";
    }


    @RequiresPermissions("ss:affairs:add")
    @Log("保存事项管理")
    @PostMapping("/save")
    @ResponseBody
    R save(AffairsDO affairsDO, @RequestParam("departId") String departId, @RequestParam("sortId") String sortId, @RequestParam(value = "templateid", required = false) String templateid) {
        affairsDO.setTemplateId(templateid);
        affairsDO.setLevel("10");
        String id = KeyUtil.genUniqueKey();
        String affairId = KeyUtil.genUniqueKey();
        affairsDO.setAffairId(affairId);
        affairsDO.setDepartId(departId);
        affairsDO.setSortId(sortId);
        affairsService.save(affairsDO);
        return R.ok();
    }

    @Log("删除事项管理")
    @RequiresPermissions("ss:affairs:remove")
    @PostMapping("/remove/{affairid}")
    @ResponseBody
    R remove(@PathVariable("affairid") String affairid) {
        int result = affairsService.remove(affairid);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }


    @Log("修改事项管理")
    @RequiresPermissions("ss:affairs:edit")
    @GetMapping("/edit/{affairid}")
    String edit(Model model, @PathVariable("affairid") String id) {
        Map<String, Object> map = affairsService.showEditAffairPage(id);
        model.addAttribute("affairs", map.get("affairs"));
        model.addAttribute("fileList", map.get("fileList"));
        model.addAttribute("departList", map.get("departList"));
        model.addAttribute("lifeList", map.get("lifeList"));
        return prefix + "/edit";

    }

    @Log("更新affairs")
    @RequiresPermissions("ss:affairs:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(AffairsDO affairsDO, @RequestParam("departId") String departId, @RequestParam("sortId") String sortId, @RequestParam("templateid") String templateId) {

        affairsDO.setTemplateId(templateId);
        affairsDO.setDepartId(departId);
        affairsDO.setSortId(sortId);
        int result = affairsService.update(affairsDO);
        if (result <= 0) {
            return R.error();
        }
        return R.ok();
    }

    @Log("批量删除affairs")
    @RequiresPermissions("ss:affairs:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") String[] id) {
        int result = affairsService.batchRemove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }


    @GetMapping("/treeview")
    public String showView() {
        return "ss/affairs/AffairsTree";
    }


    @GetMapping("/choosetemplate")
    @ResponseBody
    public List<TemplateFile> choosetemplate(@RequestParam(value = "departId") String departId) {
        Map map = new HashMap();
        map.put("departid", departId);
        List<TemplateFile> templateFileList = templateFileService.list(map);
        return templateFileList;
    }
}
