package com.yiko.ss.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiko.common.annotation.Log;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.BaseDicDO;
import com.yiko.ss.domain.Complaint;
import com.yiko.ss.service.ComplaintService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/complaint")
public class ComplaintController {
    @Autowired
    ComplaintService complaintService;
    private String prefix = "ss/complaint";

    @GetMapping(value = "/view")
    @RequiresPermissions("ss:complaint:view")
    public String view() {
        return prefix + "/complaint";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {
//        Query query = new Query(params);
//        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
//        List<Complaint> complaintList = complaintService.queryList(params);
//        PageInfo<Complaint> pageInfo = new PageInfo<>(complaintList);
//        return new PageUtils(complaintList, new Long(pageInfo.getTotal()).intValue());
        return complaintService.queryList(params);
    }

    @Log("查看投诉内容")
    @GetMapping("/content/{cId}")
    public String showContent(@PathVariable("cId") String cId, Model model) {

        Complaint complaint = complaintService.selectById(cId);
        model.addAttribute("complaint", complaint);
        return prefix + "/complaintcontent";
    }

    @Log("修改投诉内容页面")
    @RequiresPermissions("ss:complaint:edit")
    @GetMapping("/edit/{cId}")
    String edit(Model model, @PathVariable("cId") String cId) {
        Complaint complaint = complaintService.selectById(cId);
        model.addAttribute("complaint", complaint);
        return prefix + "/edit";

    }

    @Log("更新affairs")
    @RequiresPermissions("ss:affairs:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(Complaint complaint) {
        complaint.setComplaintShow("是");
        int result = complaintService.update(complaint);
        if (result >= 0) {
            return R.ok();
        }

        return R.error();
    }

    @Log("删除一条投诉")
    @RequiresPermissions("ss:complaint:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(@RequestParam("cId") String cId) {
        int result = complaintService.remove(cId);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @Log("批量删除投诉")
    @RequiresPermissions("ss:complaint:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") String[] id) {
        int result = complaintService.batchRemove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

}
