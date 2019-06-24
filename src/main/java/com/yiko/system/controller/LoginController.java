package com.yiko.system.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.yiko.common.annotation.Log;
import com.yiko.common.config.Constant;
import com.yiko.common.controller.BaseController;
import com.yiko.common.domain.FileDO;
import com.yiko.common.domain.Tree;
import com.yiko.common.exception.BDExceptionHandler;
import com.yiko.common.service.FileService;
import com.yiko.common.utils.*;
import com.yiko.ss.domain.User;
import com.yiko.system.domain.MenuDO;
import com.yiko.system.domain.UserDO;
import com.yiko.system.service.MenuService;
import com.yiko.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.x509.IPAddressName;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;


@Controller
public class LoginController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MenuService menuService;
    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;

    @Autowired
    private DefaultKaptcha captchaProducer;



    @RequestMapping("/sendMessage")
    @ResponseBody
    R sendShortMessage(String username, HttpSession session, HttpServletRequest request) {
        String ip = IPUtils.getIpAddr(request);


        R r = userService.sendShortMessage(username, ip);
        String smscode = (String) r.get("smscode");
        if (StringUtils.isNotBlank(smscode)) {
            session.setAttribute(Constant.MSG_CODE_SESSION, MD5Utils.encrypt(smscode));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (null != session.getAttribute(Constant.MSG_CODE_SESSION)) {
                        session.removeAttribute(Constant.MSG_CODE_SESSION);
                    }
                    timer.cancel();
                }
            }, 60 * 1000);
            return R.ok("发送成功");
        }


        return r;
    }


    @GetMapping({"/", ""})
    String welcome(Model model) {

        return "redirect:/login";
    }

    @Log("请求访问主页")
    @GetMapping({"/index"})
    String index(Model model) {
        List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("name", getUser().getName());
        FileDO fileDO = fileService.get(getUser().getPicId());
        if (fileDO != null && fileDO.getUrl() != null) {
            if (fileService.isExist(fileDO.getUrl())) {
                model.addAttribute("picUrl", fileDO.getUrl());
            } else {
                model.addAttribute("picUrl", "/img/photo_s.jpg");
            }
        } else {
            model.addAttribute("picUrl", "/img/photo_s.jpg");
        }
        model.addAttribute("username", getUser().getUsername());

        return "index_v1";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    /*@Log("登录")
    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username, String password, String vrifyCode, HttpServletRequest httpServletRequest) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(vrifyCode)) {
            return R.error("用户名密码或者验证码不能为空");
        }
        try {
            username = AesEncryptUtils.decrypt(username, Constant.SERCURITY_LEY);
            password = AesEncryptUtils.decrypt(password, Constant.SERCURITY_LEY);
            vrifyCode = MD5Utils.encrypt(vrifyCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        System.out.println(vrifyCode);
        if (null == vrifyCode || ("").equals(vrifyCode)) {
            return R.error("错误的验证码");
        }
        String smscode = (String) httpServletRequest.getSession().getAttribute(Constant.MSG_CODE_SESSION);
        if (null == smscode || !vrifyCode.equals(smscode)) {
            return R.error("验证码已过期，请重新获取");
        }
        try {
            subject.login(token);
            return R.ok();
        } catch (LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        } catch (ExcessiveAttemptsException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("账号不存在");
        }
    }*/

    @Log("登录")
    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return R.error("用户名密码或者验证码不能为空");
        }
        try {
            username = AesEncryptUtils.decrypt(username, Constant.SERCURITY_LEY);
            password = AesEncryptUtils.decrypt(password, Constant.SERCURITY_LEY);

        } catch (Exception e) {
            e.printStackTrace();
        }
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
            return R.ok();
        } catch (LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        } catch (ExcessiveAttemptsException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("账号不存在");
        }
    }

    @GetMapping("/logout")
    String logout() {
        ShiroUtils.logout();
        return "redirect:/login";
    }

    @GetMapping("/main")
    String main() {
        return "main";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }


}
