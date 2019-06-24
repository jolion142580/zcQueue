package com.yiko.common.controller;

import java.util.Map;

import com.yiko.common.domain.PageDO;
import com.yiko.common.service.LogService;
import com.yiko.common.utils.Query;
import com.yiko.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiko.common.domain.LogDO;

@RequestMapping("/common/log")
@Controller
public class LogController {
	@Autowired
    LogService logService;
	String prefix = "common/log";

	@GetMapping()
	String log() {
		return prefix + "/log";
	}

	@ResponseBody
	@GetMapping("/list")
    PageDO<LogDO> list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		PageDO<LogDO> page = logService.queryList(query);
		return page;
	}
	
	@ResponseBody
	@PostMapping("/remove")
    R remove(Long id) {
		if (logService.remove(id)>0) {
			return R.ok();
		}
		return R.error();
	}

	@ResponseBody
	@PostMapping("/batchRemove")
	R batchRemove(@RequestParam("ids[]") Long[] ids) {
		int r = logService.batchRemove(ids);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}
}
