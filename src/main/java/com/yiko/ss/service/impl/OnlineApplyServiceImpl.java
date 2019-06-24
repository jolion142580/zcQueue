package com.yiko.ss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.yiko.common.config.BootdoConfig;
import com.yiko.common.enums.DeptNameEnum;
import com.yiko.common.enums.OnlineapplyStateEnum;
import com.yiko.common.exception.BDException;
import com.yiko.common.service.DictService;
import com.yiko.common.service.impl.BaseServiceImpl;
import com.yiko.common.utils.*;
import com.yiko.common.utils.poi.pdf.PreviewUtils;
import com.yiko.common.utils.poi.word.MSWordTool;
import com.yiko.ss.dao.OnlineApplyDao;
import com.yiko.ss.dao.YmsFileMapper;
import com.yiko.ss.dao.YmsRecordMapper;
import com.yiko.ss.domain.*;
import com.yiko.ss.service.*;
import com.yiko.ss.vo.*;
import com.yiko.system.dao.DeptDao;
import com.yiko.system.domain.DeptDO;
import com.yiko.system.domain.UserDO;
import com.yiko.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class OnlineApplyServiceImpl extends BaseServiceImpl<OnlineApplyDO> implements OnlineApplyService {
    @Autowired
    BootdoConfig bootdoConfig;
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserService userService;

    @Autowired
    private OnlineApplyDao onlineApplyDao;

    @Autowired
    private DeptDao deptDao;
    @Autowired
    AffairsService affairsService;
    @Autowired
    AffairObjectService affairObjectService;

    @Autowired
    private OnlineApplyOpinionService onlineApplyOpinionService;

    @Autowired
    FileInfoService fileInfoService;

    @Autowired
    private DictService dictService;

    @Autowired
    private YmsRecordMapper ymsRecordMapper;

    @Autowired
    private YmsFileMapper ymsFileMapper;

    @Override
    protected void setMapping() {
        this.setMapper(this.onlineApplyDao, OnlineApplyDO.class);
    }

    @Override
    public List<OnlineApplyDO> list(Map<String, Object> map) {


        return onlineApplyDao.list(map);
    }

    @Override
    public int updateByCode(OnlineApplyDO onlineApplyDO) {
        return onlineApplyDao.updateByCode(onlineApplyDO);
    }


    @Override
    public List<OnlineApplyDO> queryCommitAffairCount(String startTime, String endTime) {
        Example example = new Example(OnlineApplyDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("iscommit", "true");
        criteria.andBetween("creattime", startTime, endTime);
        return onlineApplyDao.selectByExample(example);
    }

    @Override
    public List<OnlineApplyCountVo> calculateDateTotal(String startTime, String endTime) {
        List<OnlineApplyCountVo> list = onlineApplyDao.calculateDateTotal(startTime, endTime);
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageUtils selectPages(Map<String, Object> map) {
        UserDO userDO = ShiroUtils.getUser();
        DeptDO deptDO = deptDao.get(userDO.getDeptId());

        if (!DeptNameEnum.ZHANG_CHA_JIE_DAO.getDeptName().equals(deptDO.getName())) {
            map.put("userId", userDO.getUserId());
        }
        map.put("iscommit", "true");
        Query query = new Query(map);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<OnlineApplyDO> onlineApplyDOList = this.list(map);
        PageInfo<OnlineApplyDO> pageInfo = new PageInfo<>(onlineApplyDOList);
        for (OnlineApplyDO onlineApplyDO : onlineApplyDOList) {
            if (StringUtils.equals(onlineApplyDO.getState(), "STATE")) {
                onlineApplyDO.setState("预审中");
            }
        }
        return new PageUtils(onlineApplyDOList, new Long(pageInfo.getTotal()).intValue());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OnlineApplyDO queryUserNameAndAffairNameByOnlineId(String id) {
        return onlineApplyDao.queryUserNameAndAffairNameByOnlineId(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> showEditDetail(String id) {

        Map<String, Object> resultMap = Maps.newHashMap();
        OnlineApplyDO onlineApplyDO = this.queryUserNameAndAffairNameByOnlineId(id);
        resultMap.put("onlineApplyDO", onlineApplyDO);
        String openid = onlineApplyDO.getOpenid();
        UserInfo userInfo = userInfoService.selectByPrimaryKey(openid);
        resultMap.put("userInfo", userInfo);
//        List<DictDO> dictDOS = dictService.listByType("onlineApplyState");
//        String state = onlineApplyDO.getState();
//        if (state != null) {
//            for (DictDO dictDO : dictDOS) {
//                if (state.equals(dictDO.getValue())) {
//                    dictDO.setRemarks("checked");
//                }
//            }
//        }
//        resultMap.put("onlineApplyStates", dictDOS);

        return resultMap;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R updateOnlineApply(OnlineApplyDO onlineApplyDO) {
        //防止多人同时操作，如果有审核结果就直接return
//        OnlineApplyDO queryOnlineApplyDo = onlineApplyDao.selectByPrimaryKey(onlineApplyDO.getId());
//        if (StringUtils.equals(queryOnlineApplyDo.getState(), OnlineapplyStateEnum.YI_SHEN_HE.getMessage())) {
//            return R.error("【网上办事】这个事项已办结");
//        }

        String state = onlineApplyDO.getState();
        if (StringUtils.isBlank(state)) {
            return R.error("【网上办事】请选择审核是否通过");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", onlineApplyDO.getOpenid());
        UserInfo userInfo = userInfoService.getById(map);

        AffairsDO affairsDO = affairsService.get(onlineApplyDO.getAffairid());
        //审核通过
        if (state.equals(OnlineapplyStateEnum.YI_SHEN_HE.getMessage())) {
            Map<String, Object> affairObjectMap = new HashMap<>();
            affairObjectMap.put("affairId", affairsDO.getAffairId());
            affairObjectMap.put("objindex", onlineApplyDO.getObjindex());
            AffairObject affairObject = affairObjectService.list(affairObjectMap).get(0);

            String sendMsg = String.format("您好%s,  您所申请的%s事项已审核成功，请尽快来办理，如有疑问可拨打82590191咨询，谢谢。", userInfo.getName(), affairsDO.getAffairName());
            sendSuccessVX(sendMsg, userInfo.getName(), affairsDO.getAffairName(), affairObject.getObjname(), onlineApplyDO);
            //审核通过标记 1
            onlineApplyDO.setApprovedOrNot(OnlineapplyStateEnum.APPROVED.getMessage());

        } else if (state.equals(OnlineapplyStateEnum.YU_SHEN_BU_TONG_GUO.getMessage())) {
            //审核不通过标记 0
            onlineApplyDO.setApprovedOrNot(OnlineapplyStateEnum.NOAPPROVED.getMessage());
            onlineApplyDO.setState(OnlineapplyStateEnum.YI_SHEN_HE.getMessage());
            String sendMsg = String.format("您好%s，在线办理的%s办件不通过。", userInfo.getName(), affairsDO.getAffairName());
            sendFailVX(sendMsg, OnlineapplyStateEnum.YU_SHEN_BU_TONG_GUO.getMessage(), onlineApplyDO);
        }
        onlineApplyDO.setHandleName(ShiroUtils.getUser().getName());
        int result = this.updateNotNull(onlineApplyDO);
        if (result <= 0) {
            throw new BDException("【网上办事】修改网上办事信息失败");
        }
        return R.ok();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void affairsLimitDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +3);
        date = calendar.getTime();
        Example example = new Example(OnlineApplyDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNotNull("limitdate");
        criteria.andNotEqualTo("pushFlag", "1");
        List<OnlineApplyDO> onlineApplyDOList = onlineApplyDao.selectByExample(example);

        for (OnlineApplyDO onlineApplyDO : onlineApplyDOList) {
            if (sdf.format(date).equals(onlineApplyDO.getLimitdate())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", onlineApplyDO.getOpenid());
                UserInfo userInfo = userInfoService.getById(map);
                AffairsDO affairsDO = affairsService.get(onlineApplyDO.getAffairid());
                System.out.println("时间相同");
                Map<String, TemplateData> m = new HashMap<String, TemplateData>();
                TemplateData first = new TemplateData();
                first.setColor("#000000");
                //bymao " + affairsDO.getAffairName() + "
                first.setValue("你好" + userInfo.getName() + "，你有一项待办理事项。");
                m.put("first", first);
                TemplateData keyword1 = new TemplateData();
                keyword1.setColor("#328392");
                keyword1.setValue(affairsDO.getAffairName());
                m.put("keyword1", keyword1);

                TemplateData keyword2 = new TemplateData();
                keyword2.setColor("#328392");
                keyword2.setValue(onlineApplyDO.getCreattime());
                m.put("keyword2", keyword2);

                TemplateData remark = new TemplateData();
                remark.setColor("#929232");
                remark.setValue("您在线申请的" + affairsDO.getAffairName() + "业务将在3天后过期，请尽快前往办理！");
                m.put("remark", remark);

                WxTemplate template = new WxTemplate();
                template.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb01880d47c255669&redirect_uri=http://zcxzfwzx.chancheng.gov.cn/onlineApply!onlineApplyHistory&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");
                template.setTouser(onlineApplyDO.getOpenid());
                template.setTopcolor("#000000");
                template.setTemplate_id("YBJ79zd9P1WoAdkqOUG04DfE8M-6025vttZZq0GxUjY");
                template.setData(m);

                JSONObject jsonObject = JSONObject.fromObject(template);
                Map tempMap = new HashMap();
                tempMap.put("templateData", jsonObject.toString());
                String url = bootdoConfig.getWxServerPath() + "sendTemplate!sendTemplateMessage";
                String result = HttpClientUtil.doPost(url, tempMap);
                System.out.println("111:::" + result);
                try {
                    if (null != result && !result.equals("")) {
                        JSONObject resultData = JSONObject.fromObject(result);
                        String success = resultData.getString("success");
                        System.out.println("==事项到期提醒==" + success);
                        if (success.equals("true")) {
                            //return R.ok("通知成功");
                        } else {
                            throw new BDException("【网上办事】微信及时通知失败");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                OnlineApplyDO updateOnlineApp = new OnlineApplyDO();
                onlineApplyDO.setId(onlineApplyDO.getId());
                onlineApplyDO.setPushFlag("1");
                int res = onlineApplyDao.updateByPrimaryKeySelective(onlineApplyDO);
                if (res <= 0) {
                    throw new BDException("及时通知失败");
                }

            }

        }


    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
//    public int word2Pdf(String bookmarkjson, String tablejson, OnlineApplyDO onlineApplyDO, TemplateFile
//            templateFile) {
//        String templatePath = templateFile.getLocalpath();
//        String suffix = templatePath.substring(templatePath.lastIndexOf("."));
//        System.out.println("====================suffix========================" + suffix);
//        String savepath = bootdoConfig.getWechatfile() + onlineApplyDO.getOpenid() + "/" + onlineApplyDO.getId() + "/" + templateFile.getFilename() + "_" + UUID.randomUUID() + suffix;
//        System.out.println("===============savePath=====================" + savepath + "\n");
//        File saveFile = new File(bootdoConfig.getWechatfile() + onlineApplyDO.getOpenid() + "/" + onlineApplyDO.getId() + "/");
//        System.out.println("==============saveFile=====================" + saveFile + "\n");
//        if (!saveFile.exists()) {//如果要创建的多级目录不存在才需要创建。
//            saveFile.mkdirs();
//        }
//        MSWordTool changer = new MSWordTool();
//        String str = changer.ChangeAll(bookmarkjson, tablejson, templatePath, savepath);
//        String savePdfPath = savepath.substring(0, savepath.lastIndexOf(".")) + ".pdf";
//        System.out.println("=======================savePdfPath==================" + savePdfPath + "\n");
//        int state = PreviewUtils.officeConversionPDF(savepath, savePdfPath);
//        if (state == 1) {
//            FileInfoDO fileInfoDO = new FileInfoDO();
//            long timemillis = System.currentTimeMillis();
//            Random random = new Random();
//            float num = random.nextFloat();
//            int finalNum = (int) (num * 100000000);
//            String finalNumStr = new Integer(finalNum).toString();
//            String fileid = timemillis + finalNumStr;
//            fileInfoDO.setId(fileid);
//            fileInfoDO.setAffairid(onlineApplyDO.getAffairid());
//            fileInfoDO.setOnlineapplyid(onlineApplyDO.getId());
//            fileInfoDO.setFilename(savePdfPath.substring(savePdfPath.lastIndexOf("/") + 1));
//            fileInfoDO.setLocalpath(savePdfPath);
//            fileInfoDO.setRemark(savePdfPath.substring(savePdfPath.lastIndexOf("/") + 1, savePdfPath.lastIndexOf(".")));
//            fileInfoDO.setOpenid(onlineApplyDO.getOpenid());
//            fileInfoDO.setCreattime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
//            fileInfoService.save(fileInfoDO);
//            return 1;
//        }
//        return 0;
//    }

    @Override

    @Transactional(propagation = Propagation.REQUIRED)

    public int word2Pdf(String bookmarkjson, String tablejson, OnlineApplyDO onlineApplyDO, TemplateFile templateFile) {
        Map<String, String> mwMap = null;
        if (bookmarkjson != null) {
            bookmarkjson = getData(bookmarkjson);
            mwMap = getMWdata(bookmarkjson);
        }

        String templatePath = templateFile.getLocalpath();

        // String suffix = templatePath.substring(templatePath.lastIndexOf("."));

        // System.out.println("====================suffix========================" + suffix);

        String savepath = getSavePath(onlineApplyDO, templateFile);
        // String savepath = bootdoConfig.getWechatfile() + onlineApplyDO.getOpenid() + "/" + onlineApplyDO.getId() + "/" + templateFile.getFilename() + "_gdek" + suffix;

        System.out.println("===============savePath=====================" + savepath + "\n");

        File saveFile = new File(bootdoConfig.getWechatfile() + onlineApplyDO.getOpenid() + "/" + onlineApplyDO.getId() + "/");

        System.out.println("==============saveFile=====================" + saveFile + "\n");

        if (!saveFile.exists()) {//如果要创建的多级目录不存在才需要创建。

            saveFile.mkdirs();

        }

        MSWordTool changer = new MSWordTool();

        String str = "";

        if (mwMap != null) {

            str = changer.ChangeAll(bookmarkjson, tablejson, templatePath, savepath, mwMap);

        } else {

            str = changer.ChangeAll(bookmarkjson, tablejson, templatePath, savepath);

        }

        String savePdfPath = savepath.substring(0, savepath.lastIndexOf(".")) + ".pdf";

        System.out.println("=======================savePdfPath==================" + savePdfPath + "\n");

        int state = PreviewUtils.officeConversionPDF(savepath, savePdfPath);

        if (state == 1) {

            FileInfoDO fileInfoDO = new FileInfoDO();

            Example example = new Example(FileInfoDO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("affairid", onlineApplyDO.getAffairid());
            criteria.andEqualTo("onlineapplyid", onlineApplyDO.getId());
            criteria.andLike("filename", savePdfPath.substring(savePdfPath.lastIndexOf("/") + 1));
            List<FileInfoDO> fileInfoDOS = fileInfoService.selectByExample(example);

            long timemillis = System.currentTimeMillis();

            Random random = new Random();

            float num = random.nextFloat();

            int finalNum = (int) (num * 100000000);

            String finalNumStr = new Integer(finalNum).toString();

            String fileid = timemillis + finalNumStr;

            fileInfoDO.setId(fileid);

            fileInfoDO.setAffairid(onlineApplyDO.getAffairid());

            fileInfoDO.setOnlineapplyid(onlineApplyDO.getId());

            fileInfoDO.setFilename(savePdfPath.substring(savePdfPath.lastIndexOf("/") + 1));

            fileInfoDO.setLocalpath(savePdfPath);

            fileInfoDO.setRemark(savePdfPath.substring(savePdfPath.lastIndexOf("/") + 1, savePdfPath.lastIndexOf(".")));

            fileInfoDO.setOpenid(onlineApplyDO.getOpenid());

            fileInfoDO.setCreattime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));

            fileInfoService.save(fileInfoDO);

            return 1;

        }

        return 0;

    }

    private String getSavePath(OnlineApplyDO onlineApplyDO, TemplateFile templateFile) {
        String savePath = "";

        String templatePath = templateFile.getLocalpath();

        String suffix = templatePath.substring(templatePath.lastIndexOf("."));

        String affairid = onlineApplyDO.getAffairid();
        String onlineApplyid = onlineApplyDO.getId();
        String fileName = templateFile.getFilename() + "_gdek";

        Example example = new Example(FileInfoDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("affairid", affairid);
        criteria.andEqualTo("onlineapplyid", onlineApplyid);
        criteria.andLike("filename", "%" + fileName + "%");
        List<FileInfoDO> fileInfoDOS = fileInfoService.selectByExample(example);
        if (fileInfoDOS != null && fileInfoDOS.size() > 0) {
            for (FileInfoDO fileInfoDo : fileInfoDOS) {
                int result = fileInfoService.delete(fileInfoDo.getId());
                if (result == 1) { //删除pdf
                    FileUtil.deleteFile(fileInfoDo.getLocalpath());
                    //删除原板 docx | doc 获取模板后缀 ：suffix 修改pdf后缀为 suffix 后删除原版
                   /*
                      String pdfTemplate =fileInfoDo.getLocalpath().substring(0,fileInfoDo.getLocalpath().lastIndexOf("."))+suffix;
                        FileUtil.deleteFile(pdfTemplate);
                   */

                }
            }
        }

        savePath = bootdoConfig.getWechatfile() + onlineApplyDO.getOpenid() + "/" + onlineApplyDO.getId() + "/" + templateFile.getFilename() + "_gdek_" + UUID.randomUUID() + suffix;
        return savePath;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R submitAffair(String id, String[] fileIds) throws Exception {
        OnlineApplyDO onlineApplyDO = onlineApplyDao.selectByPrimaryKey(id);
        UserInfo userInfo = userInfoService.selectByPrimaryKey(onlineApplyDO.getOpenid());

        SubmitAffairInfoVo submitAffairInfoVo = new SubmitAffairInfoVo();
        List<MakeInfo> makeInfos = new ArrayList<MakeInfo>();
        HashMap<String, Object> data = new HashMap<>();
        String binary_img;
        BASE64Encoder base64Encoder = new BASE64Encoder();

        List<String> fileIdList = Arrays.asList(fileIds);
        for (int i = 0; i < fileIdList.size(); i++) {
            MakeInfo makeInfo = new MakeInfo();
            FileInfoDO fileInfoDO = fileInfoService.selectByKey(fileIdList.get(i));

            File file = new File(fileInfoDO.getLocalpath());
            byte[] byteData = FileUtil.loadFile(file);
            binary_img = base64Encoder.encode(byteData);

            makeInfo.setBinary_img(binary_img);
            makeInfo.setMaterial_name(fileInfoDO.getRemark().replaceAll("/", "、") + "." + fileInfoDO.getLocalpath().substring(fileInfoDO.getLocalpath().lastIndexOf(".") + 1));
            makeInfo.setRemark(fileInfoDO.getRemark().replaceAll("/", "、") + "." + fileInfoDO.getLocalpath().substring(fileInfoDO.getLocalpath().lastIndexOf(".") + 1));
            makeInfos.add(makeInfo);

        }
        submitAffairInfoVo.setMakeinfos(makeInfos);
        submitAffairInfoVo.setAffairid(onlineApplyDO.getAffairid());
        submitAffairInfoVo.setUsername(userInfo.getName());
        submitAffairInfoVo.setIdcard(userInfo.getIdcard());

        if (StringUtils.isNotBlank(userInfo.getSex())) {
            submitAffairInfoVo.setSex(Integer.parseInt(userInfo.getSex().equals("男") ? "0" : "1"));
        }
        submitAffairInfoVo.setBirthday(userInfo.getBrithday());
        submitAffairInfoVo.setPhone(userInfo.getPhone());
        submitAffairInfoVo.setMobilephone(userInfo.getPhone());
        submitAffairInfoVo.setResidence_address(userInfo.getAddress());
        submitAffairInfoVo.setLive_address("");
        submitAffairInfoVo.setObjid(onlineApplyDO.getObjindex());
        submitAffairInfoVo.setRemark("");
        data.put("json", submitAffairInfoVo);

        JSONObject ja = JSONObject.fromObject(data);
        System.out.println("-=-=-=-=" + ja.toString());

        System.out.println("----" + bootdoConfig.getYmsPath() + "main?xwl=2439KD5MA3WZ&method=submitMaterialsAudit");
        String result = HttpClientUtil.sendJSON(bootdoConfig.getYmsPath() + "main?xwl=2439KD5MA3WZ&method=submitMaterialsAudit", ja);
        System.out.println("responseStr:" + result);
        JSONObject jsonObject = JSONObject.fromObject(result);
        boolean requestResult = jsonObject.getBoolean("result");
        if (requestResult) {
            String affaircode = jsonObject.getString("curr_affaircode");
            updateAffairCodeAndInsertYms(id, affaircode, fileIds);

            System.out.println("--------提交办事事件：获取affaircode并更新成功。" + affaircode);
        } else {

            System.out.println("--------提交办事事件：" + result);

            return R.error(jsonObject.getString("msg"));
        }


        return R.ok();
    }

    public void updateAffairCodeAndInsertYms(String id, String affaircode, String[] fileIds) {
        //更新onlineapply
        OnlineApplyDO onlineApplyDO = new OnlineApplyDO();
        onlineApplyDO.setId(id);
        onlineApplyDO.setCurrAffaircode(affaircode);
        this.updateNotNull(onlineApplyDO);

        //新增
        YmsRecord ymsRecord = new YmsRecord();
        ymsRecord.setCurrAffaircode(affaircode);
        ymsRecord.setOnlineApplyId(id);
//        ymsRecord
        ymsRecordMapper.insertSelective(ymsRecord);

        //新增
        Arrays.stream(fileIds).forEach(e -> {
            YmsFile ymsFile = new YmsFile();
            ymsFile.setFileId(e);
            ymsFile.setCurrAffaircode(affaircode);
            ymsFileMapper.insertSelective(ymsFile);
        });

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R pushResult(String result) {
        System.out.println("---pushResult---" + result);
        if (result == null) {
            return R.error(1, "参数名称有误");
        }

        JSONObject jsonObject = JSONObject.fromObject(result);
        String currAffairCode = jsonObject.getString("curr_affaircode");
        if (StringUtils.isNotBlank(currAffairCode)) {
            YmsRecord ymsRecord = ymsRecordMapper.selectByPrimaryKey(currAffairCode);
            Preconditions.checkNotNull(ymsRecord);
            ymsRecord.setAuditOpinion(jsonObject.getString("audit_opinion"));
            ymsRecord.setStatueDesc(jsonObject.getString("statue_desc"));
            ymsRecord.setStatus(jsonObject.getString("status"));
            ymsRecordMapper.updateByPrimaryKeySelective(ymsRecord);

            OnlineApplyDO onlineApplyDO = new OnlineApplyDO();
            onlineApplyDO.setCurrAffaircode(currAffairCode);
            onlineApplyDO.setStatueDesc(jsonObject.getString("statue_desc"));
            this.updateByCode(onlineApplyDO);
        }

        return R.ok();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R sendWXFailMsg(OnlineApplyDO onlineApplyDO) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", onlineApplyDO.getOpenid());
        UserInfo userInfo = userInfoService.getById(map);
        AffairsDO affairsDO = affairsService.get(onlineApplyDO.getAffairid());
        if (onlineApplyDO.getOpinion() != null && !onlineApplyDO.getOpinion().equals("")) {
            String sendMsg = String.format("您好%s，在线办理的%s办件不通过。", userInfo.getName(), affairsDO.getAffairName());
            sendFailVX(sendMsg, OnlineapplyStateEnum.YU_SHEN_BU_TONG_GUO.getMessage(), onlineApplyDO);
        } else {
            return R.error("请输入审核意见");
        }
        return R.ok("发送成功");
    }


    /**
     * 审核不通过
     *
     * @param sendMsg:         发送内容包括 ，姓名->办理的事项->是否通过   例子：(你好" + userInfo.getName() + "，在线办理的" + affairsDO.getAffairName() + "办件不通过。)
     * @param onlineStateEnum: 填写OnlineapplyStateEnum枚举类对应状态 如：OnlineapplyStateEnum.YU_SHEN_BU_TONG_GUO.getMessage()
     * @param onlineApplyDO:   包括创建时间，评价内容
     */
    private void sendFailVX(String sendMsg, String onlineStateEnum, OnlineApplyDO onlineApplyDO) {

        Map<String, TemplateData> m = new HashMap<>();
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue(sendMsg);
        m.put("first", first);

        TemplateData keyword1 = new TemplateData();
        keyword1.setColor("#328392");
        keyword1.setValue(onlineApplyDO.getCreattime());
        m.put("keyword1", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#328392");
        keyword2.setValue(onlineStateEnum);
        m.put("keyword2", keyword2);

        TemplateData keyword3 = new TemplateData();
        keyword3.setColor("#328392");
        keyword3.setValue(onlineApplyDO.getOpinion());
        m.put("keyword3", keyword3);

        TemplateData remark = new TemplateData();
        remark.setColor("#929232");
        remark.setValue("具体详情请点击打开本消息查看");
        m.put("remark", remark);

        WxTemplate template = new WxTemplate();
        template.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb01880d47c255669&redirect_uri=http://zcxzfwzx.chancheng.gov.cn/onlineApply!onlineApplyHistory&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");
        template.setTouser(onlineApplyDO.getOpenid());
        template.setTopcolor("#000000");
        template.setTemplate_id("UuUvZ2ROiFFH6Z8sPjrRki4sgqkD-GbgUfSN6LWDv0M");
        template.setData(m);

        JSONObject jsonObject = JSONObject.fromObject(template);
        Map tempMap = new HashMap();
        tempMap.put("templateData", jsonObject.toString());
//        正式公众号
        String url = bootdoConfig.getWxServerPath() + "sendTemplate!sendTemplateMessage";
        String result = HttpClientUtil.doPost(url, tempMap);
        if (null != result && !result.equals("")) {
            JSONObject resultData = JSONObject.fromObject(result);
            String success = resultData.getString("success");
            if (!success.equals("true")) {
                throw new BDException("【审核办事信息】->微信通知群众失败");
            }
        }


    }


    /**
     * 审核通过
     *
     * @param sendMsg       发送内容包括 ，姓名->办理的事项->是否通过   例子：(你好" + userInfo.getName() + "，在线办理的" + affairsDO.getAffairName() + "通过。)
     * @param name          接收人姓名
     * @param affairName    事项名称
     * @param affairObjName 事项对象名称
     * @param onlineApplyDO 网上办事实例
     */
    private void sendSuccessVX(String sendMsg, String name, String affairName, String affairObjName, OnlineApplyDO onlineApplyDO) {

        Map<String, TemplateData> m = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#000000");

        first.setValue(sendMsg);

        m.put("first", first);
        TemplateData keyword1 = new TemplateData();
        keyword1.setColor("#328392");
        keyword1.setValue(name);
        m.put("keyword1", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#328392");
        keyword2.setValue(affairName);
        m.put("keyword2", keyword2);

        TemplateData keyword3 = new TemplateData();
        keyword3.setColor("#328392");
        keyword3.setValue(affairObjName);
        m.put("keyword3", keyword3);

        TemplateData keyword4 = new TemplateData();
        keyword4.setColor("#328392");
        keyword4.setValue(onlineApplyDO.getCreattime());
        m.put("keyword4", keyword4);

        TemplateData remark = new TemplateData();
        remark.setColor("#328392");
        remark.setValue(onlineApplyDO.getOpinion());
        m.put("remark", remark);


        WxTemplate template = new WxTemplate();

        template.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb01880d47c255669&redirect_uri=http://zcxzfwzx.chancheng.gov.cn/onlineApply!onlineApplyHistory&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");
        template.setTouser(onlineApplyDO.getOpenid());
        template.setTopcolor("#000000");
        template.setTemplate_id("gZ-cqCOsmeBkgHMChiGi5CAJTjdViF1uOP5TAmRbD_I");
        template.setData(m);

        JSONObject jsonObject = JSONObject.fromObject(template);
        Map tempMap = new HashMap();
        tempMap.put("templateData", jsonObject.toString());
        String url = bootdoConfig.getWxServerPath() + "sendTemplate!sendTemplateMessage";
//        System.out.println("------------url" + url);
//        System.out.println("-----------tempMap:" + tempMap.toString());

        String urlResult = HttpClientUtil.doPost(url, tempMap);
        System.out.println("------------urlResult:" + urlResult);
        if (null != urlResult && !urlResult.equals("")) {
            JSONObject resultData = JSONObject.fromObject(urlResult);
            String success = resultData.getString("success");
            if (!success.equals("true")) {
                throw new BDException("【网上办事】推送审核通过信息失败");
            }
        } else {
            throw new BDException("【网上办事】推送审核通过信息失败");
        }


    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageUtils OnlineAffairsIsChecked(String isChecked, Map<String, Object> params) {

        List<OnlineApplyDO> onlineApplyDOList = null;

        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");
        if (beginTime != "" || endTime != "") {

            if (beginTime == "") {
                params.put("beginTime", "1970-01-01");
            }
            if (endTime == "") {
                params.put("endTime", DateUtils.getMonthLastDay());
            }

        }

        Query query = new Query(params);

        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        //已审核事项
        if (StringUtils.equals(isChecked, "checked")) {
            //前端传过来的事项状态， 有 预审不通过，预审通过
            String state = (String) params.get("state");

//            搜索条件中有事项状态
            if (StringUtils.isNotBlank(state)) {
                //预审通过
                if (StringUtils.equals(state, OnlineapplyStateEnum.YU_SHEN_TONG_GUO.getMessage())) {
                    //预审通过标志
                    params.put("approvedOrNot", OnlineapplyStateEnum.APPROVED.getMessage());
                }
                //预审不通过
                if (StringUtils.equals(state, OnlineapplyStateEnum.YU_SHEN_BU_TONG_GUO.getMessage())) {
                    //预审不通过标志
                    params.put("approvedOrNot", OnlineapplyStateEnum.NOAPPROVED.getMessage());
                }
            }
            params.put("state", OnlineapplyStateEnum.YI_SHEN_HE.getMessage());
            onlineApplyDOList = onlineApplyDao.list(params);
        }
        //未审核
        else if (StringUtils.equals(isChecked, "unChecked")) {
            params.put("isCheck", OnlineapplyStateEnum.YU_SHEN_ZHONG.getMessage());
            params.put("iscommit", "true");
            onlineApplyDOList = onlineApplyDao.list(params);
            for (OnlineApplyDO onlineApplyDO : onlineApplyDOList) {
                if (StringUtils.equals(onlineApplyDO.getState(), OnlineapplyStateEnum.STATE.getMessage())
                        || StringUtils.equals(onlineApplyDO.getState(), OnlineapplyStateEnum.DAI_ZI_LIAO_SHANG_CHUAN.getMessage())) {
                    onlineApplyDO.setState(OnlineapplyStateEnum.YU_SHEN_ZHONG.getMessage());
                }
            }
        } else if (StringUtils.equals(isChecked, "search")) {
            String state = (String) params.get("state");

//            搜索条件中有事项状态
            if (StringUtils.isNotBlank(state)) {
                //审核通过
                if (StringUtils.equals(state, OnlineapplyStateEnum.YU_SHEN_TONG_GUO.getMessage())) {
                    //审核通过标识
                    params.put("approvedOrNot", OnlineapplyStateEnum.APPROVED.getMessage());
                    params.put("state", OnlineapplyStateEnum.YI_SHEN_HE.getMessage());
                }
                //预审不通过
                if (StringUtils.equals(state, OnlineapplyStateEnum.YU_SHEN_BU_TONG_GUO.getMessage())) {
                    //审核不通过标识
                    params.put("approvedOrNot", OnlineapplyStateEnum.NOAPPROVED.getMessage());
                    params.put("state", OnlineapplyStateEnum.YI_SHEN_HE.getMessage());
                }
                //预审中
                if (StringUtils.equals(state, OnlineapplyStateEnum.YU_SHEN_ZHONG.getMessage())) {
                    //预审中
                    params.put("isCheck", OnlineapplyStateEnum.YU_SHEN_ZHONG.getMessage());
                    params.put("iscommit", "true");
                    //      params.put("state", OnlineapplyStateEnum.YI_SHEN_HE.getMessage());
                }


            }


            onlineApplyDOList = onlineApplyDao.list(params);
            for (OnlineApplyDO onlineApplyDO : onlineApplyDOList) {
                if (StringUtils.equals(onlineApplyDO.getState(), "STATE")) {
                    onlineApplyDO.setState(OnlineapplyStateEnum.YU_SHEN_ZHONG.getMessage());
                }
            }
        }
        PageInfo<OnlineApplyDO> pageInfo = new PageInfo<>(onlineApplyDOList);
        return new PageUtils(onlineApplyDOList, new Long(pageInfo.getTotal()).intValue());
    }


    /**
     * cb_xxx  标签 选择框数据 选中
     * <p>
     * idCard  x,x,x,x,x,x,x,x
     * <p>
     * IDcard xxxxxxxx
     *
     * @param bookmarkjson
     * @return
     */

    private String getData(String bookmarkjson) {

        Map<String, Object> map = GsonUtil.GsonToMaps(bookmarkjson);

        Set<String> setKey = map.keySet();

        Set<String> cbKeys = new HashSet<>(); //存放cb_xxx类型的字符串

        Set<String> idKeys = new HashSet<>();//存放需要拆分的idCard_index


        for (String key : setKey) {

            if (key.indexOf("cb_") != -1) {

                cbKeys.add(key);

            } else if (key.indexOf("idCard_") != -1) {

                idKeys.add(key);

            }

        }

        //end        foreach

        if (cbKeys.isEmpty() && idKeys.isEmpty()) { //没有cb_xxx类型的字符串

            return bookmarkjson;

        }

        log.info("__cb_xxx类型不为空|idCard需要拆分__");

        /*
          需要区分 单选按钮和复选按钮
            单选按钮为的值为:字符串
            复选按钮的值为 :数组
         */
        for (String key : cbKeys) {


            Object object = map.get(key);
            String value1 = null;
            ArrayList<String> value2 = null;
            if (object instanceof String) {
                value1 = (String) object;
            } else if (object instanceof ArrayList) {
                value2 = (ArrayList<String>) object;

            }
            map.remove(key);

            if (value2 != null) {
                for (String v : value2) {
                    map.put(v, "✓");//√✓
                }
                value2 = null;
            } else {
                map.put(value1, "✓");//√
                value1 = null;
            }

        }

        for (String key : idKeys) {

            String value = (String) map.get(key);

            map.remove(key);

            char[] values = value.toCharArray();

            for (int i = 0; i < 18; i++) {

                if (i < values.length) {

                    map.put(key + "_" + (i + 1), values[i] + "");

                } else {

                    map.put(key + "_" + (i + 1), " "); //补满18位身份证ID

                }
            }
        }

        bookmarkjson = GsonUtil.GsonString(map);

        return bookmarkjson;

    }


    /**
     * 获取明文数据
     * <p>
     * mw_xxx区分数据
     *
     * @param bookmark json数据
     * @return
     */

    private Map<String, String> getMWdata(String bookmark) {

        Map<String, String> result = new HashMap<>();

        Map<String, String> map = GsonUtil.GsonToMaps(bookmark);

        Set<String> setKey = map.keySet();

        Set<String> keys = new HashSet<>();

        for (String key : setKey) {

            if (key.indexOf("mw_") != -1) {

                keys.add(key);

            }

        }

        if (keys.isEmpty()) { //没有mw_xxx类型的字符串

            return null;

        }

        log.info("__需要明文替换word__");

        for (String key : keys) {

            result.put(key, map.get(key));

        }

        return result;

    }


}
