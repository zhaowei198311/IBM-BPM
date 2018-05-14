/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.Const;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartportal.service.ProcessFormService;

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
	
	@Autowired
	private ProcessFormService processFormService;

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
	public ModelAndView startProcess(@RequestParam(value = "proUid",required = false) String proUid,
			@RequestParam(value = "proAppId",required = false) String proAppId, @RequestParam(value = "verUid",required = false) String verUid,
			@RequestParam(value = "proName",required = false) String proName,
			@RequestParam(value = "categoryName",required = false) String categoryName) {
		ModelAndView mv = new ModelAndView("process");
		mv.addObject("proUid", proUid);
		mv.addObject("proAppId", proAppId);
		mv.addObject("verUid", verUid);
		mv.addObject("proName", proName);
		mv.addObject("categoryName", categoryName);
		System.err.println(proAppId);
		mv.addObject("userId", SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		// 表单详细信息设置
		Map<String,Object> resultMap = processFormService.queryProcessForm(proAppId, proUid, verUid);
		mv.addObject("formId", resultMap.get("formId"));
		mv.addObject("actcUid", resultMap.get("actcUid"));
		mv.addObject("activityId", resultMap.get("activityId"));
		mv.addObject("activityBpdId", resultMap.get("activityBpdId"));
		return mv;
	}
	
	
	
	@RequestMapping("/processType")
	public ModelAndView processType(@RequestParam(value = "proUid",required = false) String proUid,
			@RequestParam(value = "proAppId",required = false) String proAppId, @RequestParam(value = "verUid",required = false) String verUid,
			@RequestParam(value = "proName",required = false) String proName,
			@RequestParam(value = "categoryName",required = false) String categoryName) {
		ModelAndView mv = new ModelAndView("processType");
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
	
	@RequestMapping("sa")
	public ModelAndView sa(@RequestParam(value = "proUid",required = false) String proUid) {
		System.err.println(proUid);
		ModelAndView mv = new ModelAndView("redict:menus/process");
		return mv;
	}
}
