package com.yiko.ss.controller;

import com.yiko.common.controller.BaseDoController;
import com.yiko.common.domain.DictDO;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.R;
import com.yiko.ss.domain.BlackWhiteList;
import com.yiko.ss.service.BlackWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ss/blackWhiteList")
public class BlackWhiteListConntroller extends BaseDoController<BlackWhiteList> {

    private final String prefix = "ss/blackWhiteList";

    @Autowired
    private BlackWhiteListService blackWhiteListService;

    @Override
    @PostConstruct
    public void setService() {
        this.setBaseService(this.blackWhiteListService);
    }

    @RequestMapping("index")
    public String blackWhiteList() {
        return prefix + "/blackWhiteList";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageUtils list(@RequestParam Map<String, Object> params) {
        return blackWhiteListService.queryList(params);
    }

    @PostMapping("/remove/{id}")
    @ResponseBody
    R remove(@PathVariable("id") String id) {
        return blackWhiteListService.removeById(id);
    }

    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") String id) {
        BlackWhiteList blackWhiteList = blackWhiteListService.selectById(id);
        model.addAttribute("blackWhiteList", blackWhiteList);
        return prefix + "/edit";
    }

    @PostMapping("/save")
    @ResponseBody
    R save(BlackWhiteList entry) {
        return blackWhiteListService.insert(entry);
    }
    @PostMapping("/update")
    @ResponseBody
    R update(BlackWhiteList entry) {
        return blackWhiteListService.updateById(entry);
    }

    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }
}
