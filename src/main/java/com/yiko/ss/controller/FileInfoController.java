package com.yiko.ss.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yiko.common.annotation.Log;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.controller.BaseDoController;
import com.yiko.common.service.DictService;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.FileInfoDO;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.service.FileInfoService;
import com.yiko.ss.service.OnlineApplyService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/fileInfo")
public class FileInfoController extends BaseDoController<FileInfoDO> {
    private String prefix = "ss/fileInfo";
    @Autowired
    FileInfoService fileInfoService;
    @Autowired
    private DictService dictService;
    @Autowired
    BootdoConfig bootdoConfig;
    @Autowired
    OnlineApplyService onlineApplyService;


    @Override
    @PostConstruct
    public void setService() {
        this.setBaseService(this.fileInfoService);
        //this.setPageUrl("online/approvalBusiness", "online/approvalBusinessAdd", "online/approvalBusinessEdit");
    }


    @RequiresPermissions("ss:fileInfo:fileInfo")
    @RequestMapping("")
    public String index() {
        return prefix + "/fileInfo";
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) throws Exception {

        if (null == params.get("onlineapplyid") || params.get("onlineapplyid").equals("")) {
            params.put("onlineapplyid", "onlineapplyid");
        }
        Query query = new Query(params);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<FileInfoDO> fileInfoDOList = fileInfoService.list(params);
        PageInfo<FileInfoDO> pageInfo = new PageInfo<>(fileInfoDOList);
        OnlineApplyDO onlineApplyDO = onlineApplyService.selectByKey(params.get("onlineapplyid"));
        if (onlineApplyDO != null) {

            String openid = onlineApplyDO.getOpenid();
            FileInfoDO idCardPdf = getIdCardPdf(openid);
            if (null != idCardPdf) {
                fileInfoDOList.add(idCardPdf);
            }
        }
        for (FileInfoDO fileInfo : fileInfoDOList) {
            fileInfo.setRemark(fileInfo.getRemark() + fileInfo.getFilename().substring(fileInfo.getFilename().lastIndexOf(".")));
        }

//        return new PageUtils(fileInfoDOList, new Long(pageInfo.getTotal()).intValue() + 1);
        return new PageUtils(fileInfoDOList, new Long(pageInfo.getTotal()).intValue() );
    }


    private FileInfoDO getIdCardPdf(String openId) throws Exception {
        Map<String, Object> idCardPdf = Maps.newHashMap();
        idCardPdf.put("filename", "身份证正反面.pdf");
        idCardPdf.put("openid", openId);
        List<FileInfoDO> list = fileInfoService.list(idCardPdf);
        return list.isEmpty() ? null : list.get(0);
    }


    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("ss:fileInfo:batchRemove")
    public R remove(@RequestParam("ids[]") String[] ids) {
        List<String> list = Arrays.asList(ids);
        fileInfoService.batchDelete(list, "id", FileInfoDO.class);
        return R.ok();
    }

    @Log("删除附件")
    @RequiresPermissions("ss:fileInfo:remove")
    @PostMapping("/remove/{id}")
    @ResponseBody
    R remove(@PathVariable("id") String id) {
        int result = fileInfoService.delete(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("/loadImage/{id}")
    public String loadImage(Model model, @PathVariable("id") String id) {

        FileInfoDO fileInfoDO = fileInfoService.selectByKey(id);
        String loadImgPath = fileInfoDO.getLocalpath().replace(bootdoConfig.getWechatfile(), "/wxfiles/");
        String suffix = loadImgPath.substring(loadImgPath.lastIndexOf(".") + 1);
        System.out.println("-----" + suffix);
        if (suffix.equalsIgnoreCase("pdf")) {
            model.addAttribute("localpath", fileInfoDO.getLocalpath());
            return prefix + "/pdf";
        } else {
            model.addAttribute("localpath", loadImgPath);
            return prefix + "/image";
        }

    }

    @RequestMapping(value = "/showPDF", method = RequestMethod.GET)
    public String showPDF() {
        return prefix + "/viewer";
    }

    /**
     * 返回一个PDF给页面加载
     *
     * @param response
     * @param request
     */
    @RequestMapping(value = "/displayPDF", method = RequestMethod.GET)
    public void displayPDF(HttpServletResponse response, HttpServletRequest request, @RequestParam String path) {
        try {
            System.out.println("-=-==-=-" + path);
            String pdfName = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
            File file = new File(path);//PDF保存文件路径
            FileInputStream fileInputStream = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment;fileName=" + pdfName);
            response.setContentType("multipart/form-data");
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
        } catch (Exception e) {
//            LogUtil.error("PDF读取出错!");
            e.printStackTrace();
        }
    }


    @Log("修改附件管理")
    @RequiresPermissions("ss:fileInfo:edit")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") String id) {

        FileInfoDO fileInfoDO = fileInfoService.selectByKey(id);
        model.addAttribute("fileInfoDO", fileInfoDO);

        return prefix + "/edit";

    }




}
