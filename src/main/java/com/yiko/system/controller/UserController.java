package com.yiko.system.controller;

import com.yiko.common.annotation.Log;
import com.yiko.common.config.Constant;
import com.yiko.common.controller.BaseController;
import com.yiko.common.domain.Tree;
import com.yiko.common.service.DictService;
import com.yiko.common.utils.AesEncryptUtils;
import com.yiko.common.utils.MD5Utils;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.converter.UserDO2CommonUserVO;
import com.yiko.system.domain.DeptDO;
import com.yiko.system.domain.RoleDO;
import com.yiko.system.domain.UserDO;
import com.yiko.system.service.RoleService;
import com.yiko.system.service.UserService;
import com.yiko.system.vo.CommonUserVO;
import com.yiko.system.vo.UserVO;
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

@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {
    private String prefix = "system/user";
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    DictService dictService;

    @RequiresPermissions("sys:user:user")
    @GetMapping("")
    String user(Model model) {
        return prefix + "/user";
    }

    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, Object> params) {
        return userService.list(params);
    }

    @RequiresPermissions("sys:user:add")
    @Log("添加用户")
    @GetMapping("/add")
    String add(Model model) {
        List<RoleDO> roles = roleService.list();
        model.addAttribute("roles", roles);
        return prefix + "/add";
    }

    @RequiresPermissions("sys:user:edit")
    @Log("编辑用户")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id) {
        UserDO userDO = userService.get(id);
        CommonUserVO commonUserVO = UserDO2CommonUserVO.convert(userDO);
        model.addAttribute("user", commonUserVO);
        List<RoleDO> roles = roleService.list(id);
        model.addAttribute("roles", roles);


        return prefix + "/edit";
    }

    @RequiresPermissions("sys:user:add")
    @Log("保存用户")
    @PostMapping("/save")
    @ResponseBody
    R save(UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        if (userService.save(user) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/update")
    @ResponseBody
    R update(UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.update(user) > 0) {
            return R.ok();
        }
        return R.error();
    }


    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/updatePeronal")
    @ResponseBody
    R updatePeronal(UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.updatePersonal(user) > 0) {
            return R.ok();
        }
        return R.error();
    }


    @RequiresPermissions("sys:user:remove")
    @Log("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    R remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @RequiresPermissions("sys:user:batchRemove")
    @Log("批量删除用户")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") Long[] userIds) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        int r = userService.batchremove(userIds);
        if (r > 0) {
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        return !userService.exit(params);
    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("请求更改用户密码")
    @GetMapping("/resetPwd/{id}")
    String resetPwd(@PathVariable("id") Long userId, Model model) {

        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        model.addAttribute("user", userDO);
        return prefix + "/reset_pwd";
    }

    @Log("提交更改用户密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    R resetPwd(UserVO userVO) {

        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        try {
            userService.resetPwd(userVO, getUser());
            return R.ok();
        } catch (Exception e) {
            return R.error(1, e.getMessage());
        }

    }

    @Log("首次更改用户密码")
    @PostMapping("/firstResetPwd")
    @ResponseBody
    R firstResetPwd(UserVO userVO, Model model) {
        UserDO userDO = getUser();
        userDO.setFirstLogin("1");
        model.addAttribute("user", userDO);

        try {

            int result = userService.resetPwd(userVO, getUser());
            if (result > 0) {
                userService.updateFirstLogin(userDO);
            }
            return R.ok();
        } catch (Exception e) {
            return R.error(1, e.getMessage());
        }

    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("admin提交更改用户密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    R adminResetPwd(UserVO userVO) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        try {
            userService.adminResetPwd(userVO);
            return R.ok();
        } catch (Exception e) {
            return R.error(1, e.getMessage());
        }

    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDO> tree() {
        Tree<DeptDO> tree = new Tree<DeptDO>();
        tree = userService.getTree();
        return tree;
    }

    @GetMapping("/treeView")
    String treeView() {
        return prefix + "/userTree";
    }

    @GetMapping("/personal")
    String personal(Model model) {
        UserDO userDO = userService.get(getUserId());
        model.addAttribute("user", userDO);
        model.addAttribute("hobbyList", dictService.getHobbyList(userDO));
        model.addAttribute("sexList", dictService.getSexList());
        return prefix + "/personal";
    }

    @GetMapping("/firstLoginPage")
    String firstLoginPage(Model model) {
        UserDO userDO = userService.get(getUserId());
        model.addAttribute("user", userDO);
        return prefix + "/first_login";
    }

    @PostMapping("/isFirstLogin")
    @ResponseBody
    R isFirstLogin(Model model) {
        UserDO userDO = userService.get(getUserId());
        if (null == userDO.getFirstLogin() || userDO.getFirstLogin().equals("0")) {
            model.addAttribute("user", userDO);
            return R.ok();
        }
        return null;

    }


    @ResponseBody
    @PostMapping("/uploadImg")
    R uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data, HttpServletRequest request) {
        if ("test".equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        Map<String, Object> result = new HashMap<>();
        try {
            result = userService.updatePersonalImg(file, avatar_data, getUserId());
        } catch (Exception e) {
            return R.error("更新图像失败！");
        }
        if (result != null && result.size() > 0) {
            return R.ok(result);
        } else {
            return R.error("更新图像失败！");
        }
    }
}
