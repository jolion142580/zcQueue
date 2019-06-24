package com.yiko.common.task;


import com.yiko.common.config.BootdoConfig;
import com.yiko.common.utils.HttpClientUtil;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.AffairsDO;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.domain.User;
import com.yiko.ss.domain.UserInfo;
import com.yiko.ss.service.AffairsService;
import com.yiko.ss.service.OnlineApplyService;
import com.yiko.ss.service.UserInfoService;
import com.yiko.ss.vo.TemplateData;
import com.yiko.ss.vo.WxTemplate;
import net.sf.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AffairsLimitDateJob implements Job {

    @Autowired
    OnlineApplyService onlineApplyService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AffairsService affairsService;
    @Autowired
    BootdoConfig bootdoConfig;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        onlineApplyService.affairsLimitDate();
        /*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +3);
        date = calendar.getTime();
        //System.out.println(sdf.format(date));

        List<OnlineApplyDO> onlineApplyDOList = onlineApplyService.list(new HashMap<>());
        for (OnlineApplyDO onlineApplyDO:onlineApplyDOList ) {
            if(onlineApplyDO.getLimitdate()!=null && !onlineApplyDO.getLimitdate().equals("")){
                //System.out.println("-----"+onlineApplyDO.getLimitdate());
                if(sdf.format(date).equals(onlineApplyDO.getLimitdate())){

                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("id",onlineApplyDO.getOpenid());
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
                    remark.setValue("您在线申请的"+affairsDO.getAffairName()+"业务将在3天后过期，请尽快前往办理！");
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
                    String url = bootdoConfig.getWxServerPath()+"sendTemplate!sendTemplateMessage";
                    String result = HttpClientUtil.doPost(url, tempMap);
                    System.out.println("111:::"+result);
                    try {
                        if (null != result && !result.equals("")) {
                            JSONObject resultData = JSONObject.fromObject(result);
                            String success = resultData.getString("success");
                            System.out.println("==事项到期提醒=="+success);
                            if (success.equals("true")) {
                                //return R.ok("通知成功");
                            } else {
                               // return R.error("通知失败");
                            }
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                }
            }
        }


*/

    }

}