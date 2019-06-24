package com.yiko.ss.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.annotation.Log;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.controller.BaseDoController;
import com.yiko.common.domain.DictDO;
import com.yiko.common.service.DictService;
import com.yiko.common.utils.*;
import com.yiko.ss.domain.*;
import com.yiko.ss.service.*;
import com.yiko.system.service.DeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/onlineApply")
public class OnlineApplyController extends BaseDoController<OnlineApplyDO> {
    private String prefix = "ss/onlineApply";
    @Autowired
    OnlineApplyService onlineApplyService;
    @Autowired
    AffairsService affairsService;
    @Autowired
    private DictService dictService;
    @Autowired
    private TemplateFileService templateFileService;
    @Autowired
    BootdoConfig bootdoConfig;
    @Autowired
    FileInfoService fileInfoService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    DeptService deptService;

    @Autowired
    AffairObjectService affairObjectService;

    @Autowired
    YmsRecordService ymsRecordService;

    @Autowired
    YmsFileService ymsFileService;


    @Override
    @PostConstruct
    public void setService() {
        this.setBaseService(this.onlineApplyService);
    }


    @RequiresPermissions("ss:onlineApply:onlineApply")
    @RequestMapping("")
    public String index(Model model) {
        List<DictDO> dictDOS = dictService.listByType("onlineApplyState");
        model.addAttribute("dictDOS", dictDOS);
        return prefix + "/onlineApply";
    }

    @RequestMapping("allChecked")
    public String allChecked(Model model) {
   //     List<DictDO> dictDOS = dictService.listByType("onlineApplyState");
   //     model.addAttribute("dictDOS", dictDOS);
        return prefix + "/onlineApplyIsChecked";
    }

    @RequestMapping("allUnChecked")
    public String allUnChecked(Model model) {
  //      List<DictDO> dictDOS = dictService.listByType("onlineApplyState");
  //      model.addAttribute("dictDOS", dictDOS);
        return prefix + "/onlineApplyUnChecked";
    }

    @RequestMapping("allSearch")
    public String onlineApplySearch(Model model) {
        List<DictDO> dictDOS = dictService.listByType("onlineApplyState");
        model.addAttribute("dictDOS", dictDOS);
        return prefix + "/onlineApplySearch";
    }


    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
        return onlineApplyService.selectPages(params);
    }


    @Log("删除网上办事管理")
//    @RequiresPermissions("ss:onlineApply:remove")
    @PostMapping("/remove/{onlineApplyId}")
    @ResponseBody
    R remove(@PathVariable("onlineApplyId") String onlineApplyId) {
        int result = onlineApplyService.delete(onlineApplyId);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @Log("修改网上办事管理")
//    @RequiresPermissions("ss:onlineApply:edit")
    @GetMapping("/edit/{id}")
    ModelAndView edit(Model model, @PathVariable("id") String id) {

        return  new ModelAndView(prefix + "/edit", onlineApplyService.showEditDetail(id));
    }

    @Log("更新onlineApply")
//    @RequiresPermissions("ss:onlineApply:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(OnlineApplyDO onlineApplyDO) {
        return onlineApplyService.updateOnlineApply(onlineApplyDO);
    }

    @Log("查看提交的历史记录")
    @GetMapping("/history/{id}")
    String history(Model model, @PathVariable("id") String id) {
        model.addAttribute("onlineApplyId", id);
        return prefix + "/history";
    }

    @GetMapping("/historyByRecord")
    @ResponseBody
    public PageUtils recordList(@RequestParam Map<String, Object> params) {

        Query query = new Query(params);
        if (params.get("onlineApplyId") == null || params.get("onlineApplyId").equals("")) {
            params.put("onlineApplyId", "onlineApplyId");
        }
        YmsRecord ymsRecord = new YmsRecord();
        ymsRecord.setOnlineApplyId((String) params.get("onlineApplyId"));
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<YmsRecord> ymsRecordList = ymsRecordService.listByNotNull(ymsRecord);
        PageInfo<YmsRecord> pageInfo = new PageInfo<>(ymsRecordList);
        return new PageUtils(ymsRecordList, new Long(pageInfo.getTotal()).intValue());
    }

    @GetMapping("/ymsFileList")
    @ResponseBody
    public PageUtils ymsFileList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);

        String currAffaircode = (String) params.get("currAffaircode");
        if (StringUtils.isBlank(currAffaircode)) {
            params.put("currAffaircode", "currAffaircode");
        }

        YmsFile ymsFile = new YmsFile();
        ymsFile.setCurrAffaircode((String) params.get("currAffaircode"));
        List<YmsFile> ymsFileList = ymsFileService.listByNotNull(ymsFile);

        List<FileInfoDO> fileInfoList = new ArrayList<>();
        ymsFileList.stream().forEach(e -> {
            if (StringUtils.isNotBlank(e.getFileId())) {
                FileInfoDO fileInfoDO = fileInfoService.selectByKey(e.getFileId());
                if (fileInfoDO != null) {
                    fileInfoList.add(fileInfoDO);
                }
            }
        });

        PageInfo<FileInfoDO> pageInfo = new PageInfo<>(fileInfoList);
        return new PageUtils(fileInfoList, new Long(pageInfo.getTotal()).intValue());
    }


    @Log("查看网上办事填写表单")
    //@RequiresPermissions("ss:onlineApply:loadPDF")
    @PostMapping("/loadPDF/{id}")
    @ResponseBody
    R loadPDF(@PathVariable("id") String id) throws Exception {

        System.out.println("-=--=-" + id);
        OnlineApplyDO onlineApplyDO = onlineApplyService.selectByKey(id);

        String bookmarkjson = onlineApplyDO.getOnlinedata();

        if(bookmarkjson == null){ //可能直接上传文件没填写的，不必生成文档
            return R.error("该事项没有填写，请查看上传图片");
        }

        String tablejson = "";

        String objIndex = onlineApplyDO.getObjindex();
        String affairId = onlineApplyDO.getAffairid();

        //事项对象
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("affairId", affairId);
        objectMap.put("objindex", objIndex);
        AffairObject affairObject = affairObjectService.list(objectMap).get(0);
        System.out.println("====================affairObject==============" + affairObject + "\n");

        AffairsDO affairsDO = affairsService.get(onlineApplyDO.getAffairid());

        String templateId = affairObject.getTemplateid();

        String templateId2 = affairObject.getTemplateid1() == null ? "" : affairObject.getTemplateid1();

        TemplateFile templateFile = getTemplateFile(templateId);

        TemplateFile templateFile2 = getTemplateFile(templateId2);

        int result = onlineApplyService.word2Pdf(bookmarkjson, tablejson, onlineApplyDO, templateFile);
        int result2 = 0;

        if (bookmarkjson != "" && templateFile2 != null) {
            result2 = onlineApplyService.word2Pdf(bookmarkjson, tablejson, onlineApplyDO, templateFile2);
            System.out.println("=================bookmarkjson != \"\" && templateFile1 != null================");
            if (result > 0 && result2 > 0) {
                return R.ok();
            }
        } else {
            if (result > 0) {
                return R.ok();
            }
        }
        return R.error("转换出错");

    }

    private TemplateFile getTemplateFile(String templateId) {
        if (StringUtils.isNotBlank(templateId)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", templateId);
            TemplateFile templateFile = templateFileService.get(map);
            return templateFile;
        }
        return null;

    }


    @Log("提交办事")
    //@RequiresPermissions("ss:onlineApply:loadPDF")
    @PostMapping("/submitAffair")
    @ResponseBody
    R submitAffair(@RequestParam("id") String id, @RequestParam("fileIds[]") String[] fileIds) throws Exception {

        return onlineApplyService.submitAffair(id, fileIds);
    }


    @Log("一门式返回预审结果")
    @PostMapping("/pushResult")
    @ResponseBody
    R pushResult(@RequestParam(value = "result", required = false) String result) {
        return onlineApplyService.pushResult(result);

    }


    @Log("查询全部未审核或已审核事项")
    @GetMapping("/{isChecked}")
    /**
     * param isChecked
     * checked 代表已审核
     * unChecked 未审核
     */
    @ResponseBody
    public PageUtils findAllIsCheckEd(@PathVariable("isChecked") String isChecked, @RequestParam Map<String, Object> params) {

        if (StringUtils.isBlank(isChecked)) {
            throw new BDException("【网上办事事项查询】参数错误");
        }
        return onlineApplyService.OnlineAffairsIsChecked(isChecked, params);

    }


}
