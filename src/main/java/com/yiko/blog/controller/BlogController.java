package com.yiko.blog.controller;

import com.yiko.blog.domain.ContentDO;
import com.yiko.blog.service.ContentService;
import com.yiko.common.utils.DateUtils;
import com.yiko.common.utils.PageUtils;
import com.yiko.common.utils.Query;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yiko 1992lcg@163.com
 */
@RequestMapping("/blog")
@Controller
public class BlogController {
	@Autowired
    ContentService bContentService;

	@GetMapping()
	String blog() {
		return "blog/index/main";
	}

	@ResponseBody
	@GetMapping("/open/list")
	public PageUtils opentList(@RequestParam Map<String, Object> params) {
		/*Query query = new Query(params);
		List<ContentDO> bContentList = bContentService.list(query);
		int total = bContentService.count(query);
		PageUtils pageUtils = new PageUtils(bContentList, total);
		return pageUtils;*/

		Query query = new Query(params);
		PageHelper.startPage(query.getPageNumber(),query.getPageSize());
		List<ContentDO> bContentList = bContentService.list(query);
		PageInfo<ContentDO> pageInfo =new PageInfo<>(bContentList);
		PageUtils pageUtil = new PageUtils(bContentList,new Long(pageInfo.getTotal()).intValue());
		return new  PageUtils(pageInfo.getList(),new Long(pageInfo.getTotal()).intValue());
	}

	@GetMapping("/open/post/{cid}")
	String post(@PathVariable("cid") Long cid, Model model) {
		ContentDO bContentDO = bContentService.get(cid);
		model.addAttribute("bContent", bContentDO);
		model.addAttribute("gtmModified", DateUtils.format(bContentDO.getGtmModified()));
		return "blog/index/post";
	}
	@GetMapping("/open/page/{categories}")
	String about(@PathVariable("categories") String categories, Model model) {
		Map<String, Object> map = new HashMap<>(16);
		map.put("categories", categories);
		ContentDO bContentDO =null;
		if(bContentService.list(map).size()>0){
			 bContentDO = bContentService.list(map).get(0);
		}
		model.addAttribute("bContent", bContentDO);
		return "blog/index/post";
	}
}
