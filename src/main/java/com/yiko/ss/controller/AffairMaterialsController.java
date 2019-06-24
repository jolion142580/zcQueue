package com.yiko.ss.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.yiko.common.annotation.Log;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.domain.Tree;
import com.yiko.common.utils.*;
import com.yiko.ss.domain.*;
import com.yiko.ss.service.AffairMaterialsService;
import com.yiko.ss.service.AffairObjectService;
import com.yiko.ss.service.AffairsService;
import com.yiko.ss.service.OnlineApplyService;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/affairmaters")
public class AffairMaterialsController {
    private static final String prefix = "ss/affairmaters";

    @Autowired
    BootdoConfig bootdoConfig;

    @Autowired
    AffairMaterialsService affairMaterialsService;

    @Autowired
    AffairsService affairsService;

    @Autowired
    AffairObjectService affairObjectService;

    @Autowired
    OnlineApplyService onlineApplyService;

    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, Object> params) {
        return affairMaterialsService.queryList(params);
    }

    @GetMapping("/view")
    @RequiresPermissions("ss:maters:view")
    public String view() {
        return prefix + "/affairmaters";
    }


    @GetMapping("/add/{objId}")
    @Log("添加事项材料")
    @RequiresPermissions("ss:maters:add")
    public String add(@PathVariable("objId") String objId, Model model) {
        if (objId.indexOf("objid") == -1) {
            return null;
        }
        objId = objId.substring(5);
        AffairObject affairObject = affairObjectService.get(objId);
        if (affairObject == null) {
            throw new RuntimeException("请选择事项分类");
        }
        model.addAttribute("objId", objId);
        return prefix + "/add";
    }

    @RequiresPermissions("ss:maters:edit")
    @Log("修改事项材料")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        AffairMaterials affairMaterials = affairMaterialsService.selectOneByCondition(map);
        if (affairMaterials == null) {
            throw new RuntimeException("修改事项分类失败");
        }
        model.addAttribute("affairMaterials", affairMaterials);
        return prefix + "/edit";

    }

    @Log("删除事项管理")
    @RequiresPermissions("ss:maters:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(@RequestParam("id") String id) {
        int result = affairMaterialsService.remove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @Log("更新事项材料")
    @RequiresPermissions("ss:maters:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(@RequestParam("affairMaterials") String affairMaterials, @RequestParam(value = "file", required = false) MultipartFile file) {
//        JSONObject jsonObject = JSONObject.fromObject(affairMaterials);
//        AffairMaterials affairMaterials1 = (AffairMaterials) JSONObject.toBean(jsonObject, AffairMaterials.class);
//
//        if (file != null) {
//            String saveName = KeyUtil.genUniqueKey();
//            String fileName = file.getOriginalFilename();
//            String localPath = bootdoConfig.getTable() + saveName + fileName;
//            affairMaterials1.setLocalpath(localPath);
//            boolean flag = updateOrUploadFile(fileName);
//            if (!flag) {
//                return R.error("只能上传.doc  .docx  .xls  类型的文件!");
//            }
//
//            try {
//                FileUtil.uploadFile(file.getBytes(), bootdoConfig.getTable(), saveName + fileName);
//            } catch (Exception e) {
//                return R.error();
//            }
//        }
//        int result = affairMaterialsService.update(affairMaterials1);
//        if (result <= 0) {
//            return R.error();
//        }
//        return R.ok();
        return affairMaterialsService.update(affairMaterials, file);
    }

    @GetMapping("/checkAffair")
    @ResponseBody
    R checkAffair(@RequestParam("objId") String objId) {
        if (objId.indexOf("objid") == -1) {
            return R.error("请选择事项分类");
        }
        objId = objId.substring(5);
        AffairObject affairObject = affairObjectService.get(objId);
        if (affairObject == null) {
            return R.error();
        }
        return R.ok();
    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<Object> getTree() {
        return affairMaterialsService.getTreeAndAffairType();
    }


    @PostMapping("/save")
    @Log("保存事项材料")
    @ResponseBody
    public R save(@RequestParam("affairMaterials") String affairMaterials, @RequestParam("objId") String objId, @RequestParam(value = "file", required = false) MultipartFile file) {
        return affairMaterialsService.save(affairMaterials, objId, file);

    }

    @Log("批量删除事项材料")
    @RequiresPermissions("ss:maters:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") String[] id) {
        int result = affairMaterialsService.batchRemove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @Log("删除文档地址")
//    @RequiresPermissions("ss:maters:remove")
    @PostMapping("/removePath")
    @ResponseBody
    R removePath(@RequestParam("id") String id) {
        int result = affairMaterialsService.removePath(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("/loadMaterials")
    @ResponseBody
    PageUtils loadMaterials(@RequestParam Map<String, Object> params) {
        OnlineApplyDO onlineApplyDO = new OnlineApplyDO();
        onlineApplyDO.setId(params.get("onlineApplyId")+"");
        onlineApplyDO = onlineApplyService.selectByKey(onlineApplyDO);
        return affairMaterialsService.listByAffairId(onlineApplyDO.getAffairid(),onlineApplyDO.getObjindex());
    }

    @GetMapping("/viewMaterials/{id}")
    public String viewMaterials(@PathVariable("id") String id, Model model) {
        model.addAttribute("onlineApplyId",id);
        return "ss/onlineApply/materials";
    }
}
