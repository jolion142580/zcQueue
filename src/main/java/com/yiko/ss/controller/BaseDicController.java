package com.yiko.ss.controller;

import com.yiko.common.annotation.Log;
import com.yiko.common.domain.DictDO;
import com.yiko.common.enums.SSEnum;
import com.yiko.common.exception.BootDoException;
import com.yiko.common.service.DictService;
import com.yiko.common.utils.KeyUtil;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.service.BaseDicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/ss/basedic")
@Controller
public class BaseDicController {
    private String prefix = "ss/basedic";

    @Autowired
    BaseDicService baseDicService;

    @Autowired
    DictService dictService;

    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<BaseDicDO> baseDicDOList = baseDicService.queryList(params);
        PageInfo<BaseDicDO> pageInfo = new PageInfo<>(baseDicDOList);
        return new PageUtils(baseDicDOList, new Long(pageInfo.getTotal()).intValue());
    }

    @RequiresPermissions("ss:basedic:basedic")
    @GetMapping("/view")
    public String View() {
        return prefix + "/basedic";
    }


    @RequiresPermissions("ss:basedic:add")
    @GetMapping("/add")
    @Log("添加ss_base_dic")
    public String add(Model model) {
        List<DictDO> dictDOS = dictService.listByType("basedicType");
        model.addAttribute("type",dictDOS);
        return prefix + "/add";
    }

    @RequiresPermissions("ss:basedic:add")
    @Log("保存ss_base_dic")
    @PostMapping("/save")
    @ResponseBody
    R save(BaseDicDO baseDicDO) {
        String id = KeyUtil.genUniqueKey();
        String baseDicId = KeyUtil.genUniqueKey();
        baseDicDO.setId(id);
        baseDicDO.setBaseDicId(baseDicId);
        int result = baseDicService.save(baseDicDO);
        if (result <= 0) {
//            throw new BootDoException(SSEnum.SAVE_ERROR);
            return R.error(1, "保存失败");
        }
        return R.ok();
    }

    @RequiresPermissions("ss:basedic:edit")
    @Log("修改BaseDic")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        BaseDicDO baseDicDO = baseDicService.get(map);
        if (baseDicDO == null) {
            throw new BootDoException(SSEnum.FIND_ERROR);
        }
        List<DictDO> dictDOS = dictService.listByType("basedicType");
        String baseDicType = baseDicDO.getBaseDicType();
        if (baseDicType != null) {
            for (DictDO dictDO : dictDOS) {
                if (baseDicType.equals(dictDO.getValue())) {
                    dictDO.setRemarks("checked");
                }
            }
        }
        model.addAttribute("type",dictDOS);
        model.addAttribute("basedic", baseDicDO);
        return prefix + "/edit";

    }


    @RequiresPermissions("ss:basedic:edit")
    @Log("更新BaseDic")
    @PostMapping("/update")
    @ResponseBody
    R update(BaseDicDO baseDicDO) {
        int result = baseDicService.update(baseDicDO);
        if (result <= 0) {
            return R.error();
        }
        return R.ok();
    }

    @RequiresPermissions("ss:basedic:remove")
    @Log("删除BaseDic")
    @PostMapping("/remove/{id}")
    @ResponseBody
    R remove(@PathVariable("id") String id) {
        int result = baseDicService.remove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @RequiresPermissions("ss:basedic:batchRemove")
    @Log("批量删除BaseDic")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") String[] id) {
        int result = baseDicService.batchRemove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }



}
