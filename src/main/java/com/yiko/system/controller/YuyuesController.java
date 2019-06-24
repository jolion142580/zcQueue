package com.yiko.system.controller;

import com.yiko.common.controller.BaseDoController;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import com.yiko.system.domain.YuyueDO;
import com.yiko.system.service.YuyuesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequestMapping("/sys/yuyues")
@Controller
public class YuyuesController extends BaseDoController<YuyueDO>  {
	private String prefix="system/yuyues"  ;
	@Autowired
	YuyuesService yuyuesService;

	@Override
	@PostConstruct
	public void setService()
	{
		this.setBaseService(this.yuyuesService);
		//this.setPageUrl("online/approvalBusiness", "online/approvalBusinessAdd", "online/approvalBusinessEdit");
	}

	@RequiresPermissions("sys:yuyues:yuyues")
	@RequestMapping("")
	public String index() {
		return prefix+"/yuyues";
	}

	@GetMapping("/list")
	@ResponseBody
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<YuyueDO> yuyuesList = yuyuesService.list(query);
		int total = yuyuesService.count(query);
		PageUtils pageUtil = new PageUtils(yuyuesList, total);
		return pageUtil;
	}

	@GetMapping("/add")
	@RequiresPermissions("sys:yuyues:add")
	public String add() {
		return prefix+"/add";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("sys:yuyues:add")
	public R save(YuyueDO yuyues) {

		if (yuyuesService.save(yuyues) > 0) {
			return R.ok();
		}
		return R.error();
	}


	@GetMapping("/edit/{id}")
	@RequiresPermissions("sys:yuyues:edit")
	public String edit(@PathVariable("id") String id, Model model) {

		YuyueDO yuyues = yuyuesService.selectByKey(id);

		model.addAttribute("yuyues", yuyues);
		return prefix+"/edit";
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sys:yuyues:edit")
	public R update(YuyueDO yuyues) {
		yuyuesService.updateNotNull(yuyues);
		return R.ok();
	}

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:yuyues:remove")
    public R remove(String id) {
        if (yuyuesService.delete(id)> 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:yuyues:batchRemove")
    public R remove(@RequestParam("ids[]") String[] ids) {
		List<String> list = Arrays.asList(ids);
        yuyuesService.batchDelete(list, "id", YuyueDO.class);
        return R.ok();
    }

}
