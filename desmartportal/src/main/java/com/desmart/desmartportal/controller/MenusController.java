/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Title: 跳转WEB-INFO下jsp页面的 控制层
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年4月27日
 */
@Controller
@RequestMapping("/menus")
public class MenusController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/backlog")
	public ModelAndView backlog() {
		ModelAndView mv = new ModelAndView("backlog");
		return mv;
	}

	@RequestMapping("/backlogDetail")
	public ModelAndView backlogDetail() {
		ModelAndView mv = new ModelAndView("backlog_detail");
		return mv;
	}
	
	/**
	 * 发起流程
	 * @param proUid 流程id
	 * @param proAppId 流程应用库id
	 * @param verUid 流程版本id
	 * @param proName 流程名称
	 * @param categoryName 流程分类名称
	 * @return
	 */
	@RequestMapping("/startProcess")
	public ModelAndView startProcess(@RequestParam(value = "proUid") String proUid,
			@RequestParam(value = "proAppId") String proAppId, @RequestParam(value = "verUid") String verUid,
			@RequestParam(value = "proName") String proName,
			@RequestParam(value = "categoryName") String categoryName) {
		ModelAndView mv = new ModelAndView("formset");
		mv.addObject("proUid", proUid);
		mv.addObject("proAppId", proAppId);
		mv.addObject("verUid", verUid);
		mv.addObject("proName", proName);
		mv.addObject("categoryName", categoryName);
		return mv;
	}
	
	@RequestMapping("queryProcess")
	public ModelAndView queryProcess() {
		ModelAndView mv = new ModelAndView("query_process");
		return mv;
	}
	
	
}
