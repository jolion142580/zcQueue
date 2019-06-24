package com.yiko.ss.controller;

import com.yiko.common.annotation.Log;
import com.yiko.common.controller.BaseController;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.converter.UserInfoDO2UserInfoVO;
import com.yiko.ss.domain.UserInfo;
import com.yiko.ss.service.UserInfoService;
import com.yiko.ss.vo.UserInfoVO;
import com.yiko.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ss/userinfo/")
public class UserInfoController extends BaseController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserService userService;

    private static final String prefix = "ss/userinfo";

    @GetMapping(value = "/view")
    @RequiresPermissions("ss:userinfo:view")
    public String view() {
        return prefix + "/userinfo";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageUtils queryList(@RequestParam Map<String, Object> params) {

        return userInfoService.list(params);

    }

    @Log("修改用户管理页面")
    @RequiresPermissions("ss:userinfo:edit")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        UserInfo userInfo = userInfoService.getById(map);

        if (userInfoService.hasRole()) {
            model.addAttribute("userinfo", userInfo);
        } else {
            UserInfoVO userInfoVO = UserInfoDO2UserInfoVO.convertAndUpdate(userInfo);
            model.addAttribute("userinfo", userInfoVO);
        }
        return prefix + "/edit";

    }


    @Log("更新用户信息")
    @RequiresPermissions("ss:userinfo:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(UserInfo userInfo) {
        int result = userInfoService.updateById(userInfo);
        if (result >= 0) {
            return R.ok();
        }
        return R.error();
    }

    @Log("删除一条用户信息")
    @RequiresPermissions("ss:userinfo:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(@RequestParam("id") String id) {
        int result = userInfoService.remove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }

    @Log("批量删除用户信息")
    @RequiresPermissions("ss:complaint:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") String[] id) {
        int result = userInfoService.batchRemove(id);
        if (result > 0) {
            return R.ok();
        }
        return R.error();
    }


    @PostMapping("/test")
    @ResponseBody
    R test() {
        return R.ok().put("data", "abc");
    }

}
