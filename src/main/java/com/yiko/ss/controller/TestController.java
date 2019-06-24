package com.yiko.ss.controller;

import com.yiko.common.annotation.Log;
import com.yiko.ss.dao.OnlineApplyDao;
import com.yiko.ss.domain.OnlineApplyDO;
import com.yiko.ss.service.OnlineApplyService;
import com.yiko.system.dao.UserDao;
import com.yiko.system.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @Value("${yiko.uploadPath:13123123}")
    private String address;
    @Autowired
    OnlineApplyService onlineApplyService;

    @Autowired
    OnlineApplyDao onlineApplyDao;

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/testCri")
    @ResponseBody
    @Log("=========testCri============")
    public OnlineApplyDO testCri() {
        Example example = new Example(OnlineApplyDO.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", "123");
        OnlineApplyDO onlineApplyDO = new OnlineApplyDO();
        onlineApplyDO.setId("123");
        OnlineApplyDO o = onlineApplyDao.selectOne(onlineApplyDO);

        List<OnlineApplyDO> on = onlineApplyService.selectByExample(example);
        return o;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @RequestMapping("/testVal")
    @ResponseBody
    public String testVal() {
        return address;
    }


    @RequestMapping("/testlimitdate")
    @ResponseBody
    public void testlimidate() {
        onlineApplyService.affairsLimitDate();
    }


}
