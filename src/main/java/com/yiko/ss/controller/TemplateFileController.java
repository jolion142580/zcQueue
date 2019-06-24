package com.yiko.ss.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.annotation.Log;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.domain.Tree;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.domain.TemplateFile;
import com.yiko.ss.service.AffairsService;
import com.yiko.ss.service.BaseDicService;
import com.yiko.ss.service.TemplateFileService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/templatefile")
public class TemplateFileController {
    @Autowired
    TemplateFileService templateFileService;

    @Autowired
    BaseDicService baseDicService;

    @Autowired
    BootdoConfig bootdoConfig;

    @Autowired
    AffairsService affairsService;


    private static final String prefix = "ss/templatefile";


    @RequiresPermissions("ss:templateFile:view")
    @GetMapping("/view")
    public String View() {
        return prefix + "/templatefile";
    }


    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {

        Query query = new Query(params);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<TemplateFile> templateFileList = templateFileService.list(params);
        PageInfo<TemplateFile> pageInfo = new PageInfo<>(templateFileList);
        return new PageUtils(templateFileList, new Long(pageInfo.getTotal()).intValue());
    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<BaseDicDO> getTree() {
        return templateFileService.getTree();
    }

    @GetMapping("/checkDepart")
    @ResponseBody
    R checkDepart(@RequestParam("departId") String departId) {
        Map map = new HashMap();
        map.put("baseDicType", "depart");
        map.put("baseDicId", departId);

        BaseDicDO baseDicDO = baseDicService.get(map);
        if (baseDicDO != null) {
            return R.ok();
        }
        return R.error();

    }


    @GetMapping("/add/{departId}")
    @Log("添加事项材料")
    @RequiresPermissions("ss:templateFile:add")
    public String add(@PathVariable("departId") String departId, Model model) {
        Map map = new HashMap();
        map.put("departId", departId);
        return prefix + "/add";
    }


    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("ss:templateFile:add")
    R save(@RequestParam("templatefile") String templatefile, @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        return templateFileService.saveOrUpdate(templatefile, file);
    }

    @RequiresPermissions("ss:templateFile:edit")
    @Log("模板修改")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        TemplateFile templateFile = templateFileService.get(map);
        if (templateFile == null) {
            throw new RuntimeException("没有找到相关模板");
        }
        model.addAttribute("templatefile", templateFile);
        return prefix + "/edit";

    }

    @RequiresPermissions("ss:templateFile:edit")
    @Log("更新模板")
    @PostMapping("/update")
    @ResponseBody
    R update(@RequestParam("templatefile") String templatefile, @RequestParam(value = "file", required = false) MultipartFile file) {
        return templateFileService.saveOrUpdate(templatefile, file);
    }

    @RequiresPermissions("ss:templateFile:remove")
    @Log("删除模板")
    @PostMapping("/remove")
    @ResponseBody
    R remove(@RequestParam("id") String id) {
        templateFileService.remove(id);

        return R.ok();
    }

    @RequiresPermissions("ss:templateFile:batchRemove")
    @Log("批量删除模板")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") String[] ids) {

        int result = templateFileService.batchRemove(ids);
        affairsService.updateByTemplateIds(ids);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }


}
