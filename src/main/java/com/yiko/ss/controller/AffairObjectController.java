package com.yiko.ss.controller;

import com.google.common.base.Preconditions;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.yiko.common.annotation.Log;
import com.yiko.common.domain.Tree;
import com.yiko.common.enums.SSEnum;
import com.yiko.common.exception.BootDoException;
import com.yiko.common.utils.KeyUtil;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.AffairObject;
import com.yiko.ss.domain.AffairsDO;
import com.yiko.ss.domain.TemplateFile;
import com.yiko.ss.service.AffairMaterialsService;
import com.yiko.ss.service.AffairObjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.ss.service.AffairsService;
import com.yiko.ss.service.TemplateFileService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/affairobject")
public class AffairObjectController {
    private final String prefix = "ss/affairobject";
    @Autowired
    AffairObjectService affairObjectService;

    @Autowired
    AffairsService affairsService;

    @Autowired
    AffairMaterialsService affairMaterialsService;

    @Autowired
    TemplateFileService templateFileService;

    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
        return affairObjectService.pageList(params);
    }

    @GetMapping("/view")
    @RequiresPermissions("ss:affairobject:view")
    public String view() {
        return prefix + "/affairobject";
    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<Object> getTree() {
        return affairObjectService.getTree();
    }

    @GetMapping("/checkAffair")
    @ResponseBody
    R checkAffair(@RequestParam("affairId") String affairId) {
        AffairsDO affairsDO = affairsService.get(affairId);
        if (affairsDO == null) {
            return R.error();
        }
        return R.ok();
    }


    @Log("修改事项分类")
    @RequiresPermissions("ss:affairObject:edit")
    @GetMapping("/edit/{objid}")
    String edit(Model model, @PathVariable("objid") String objId) {
        Map<String, Object> map = affairObjectService.updateAffairType(objId);
        model.addAttribute("affairobject", map.get("affairobject"));
        model.addAttribute("templateFileList", map.get("templateFileList"));
        return prefix + "/edit";
    }

    @RequiresPermissions("ss:affairObject:edit")
    @Log("更新事项对象分类")
    @PostMapping("/update")
    @ResponseBody
    R save(AffairObject affairObject, @RequestParam(value = "templateid", required = false) String templateid, @RequestParam(value = "templateid1", required = false) String templateid1) {
        affairObject.setTemplateid(templateid);
        if (StringUtils.isNotBlank(templateid1))
            affairObject.setTemplateid1(templateid1);
        int result = affairObjectService.update(affairObject);
        if (result > 0) {
            return R.ok();
        } else {
            return R.error("更新事项对象分类失败");
        }

    }

    @Log("删除AffairObject")
    @RequiresPermissions("ss:affairObject:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(@RequestParam("objid") String objId) {
        int result = affairObjectService.remove(objId);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }


    @Log("批量删除affairs")
    @RequiresPermissions("ss:affairObject:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("objid[]") String[] objid) {
        int result = affairObjectService.batchRemove(objid);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }


}
