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
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartportal.common.BpmStatus;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhDraftsService;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * Title: 跳转WEB-INFO下jsp页面的 控制层 带参
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
	private DhProcessFormService dhProcessFormService;
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	
	@Autowired
	private DhDraftsService dhDraftsService;

	@RequestMapping("/index")
	public String index() {
		return "desmartportal/index";
	}

	@RequestMapping("/backlog")
	public ModelAndView backlog() {
		ModelAndView mv = new ModelAndView("desmartportal/backlog");
		return mv;
	}

	@RequestMapping("/backlogDetail")
	public ModelAndView backlogDetail() {
		ModelAndView mv = new ModelAndView("desmartportal/backlog_detail");
		return mv;
	}
	
	/**
	 * 新建一个流程页面
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
		
		
		
		ModelAndView mv = new ModelAndView("desmartportal/process");
		mv.addObject("proUid", proUid);
		mv.addObject("proAppId", proAppId);
		mv.addObject("verUid", verUid);
		mv.addObject("proName", proName);
		mv.addObject("categoryName", categoryName);
		System.err.println(proAppId);
		mv.addObject("userId", SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		// 找到当前环节
		BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
		bpmActivityMeta.setBpdId(proUid); 
		bpmActivityMeta.setProAppId(proAppId);
		bpmActivityMeta.setSnapshotId(verUid);
		bpmActivityMeta.setActivityType(BpmActivityMeta.ACTIVITY_TYPE_START);
		List<BpmActivityMeta> resultList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
		for (BpmActivityMeta bpmActivityMeta2 : resultList) {
			mv.addObject("bpmActivity",bpmActivityMeta2);
		}
		// 
		
		// 表单详细信息设置
		Map<String,Object> resultMap = dhProcessFormService.queryProcessForm(proAppId, proUid, verUid);
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
		ModelAndView mv = new ModelAndView("desmartportal/processType");
		mv.addObject("proUid", proUid);
		mv.addObject("proAppId", proAppId);
		mv.addObject("verUid", verUid);
		mv.addObject("proName", proName);
		mv.addObject("categoryName", categoryName);
		return mv;
	}
	
	@RequestMapping("queryProcess")
	public ModelAndView queryProcess() {
		ModelAndView mv = new ModelAndView("desmartportal/query_process");
		return mv;
	}
	
	@RequestMapping("finishProcess")
	public ModelAndView finishProcess() {
		ModelAndView mv = new ModelAndView("desmartportal/finished");
		return mv;
	}
	
	@RequestMapping("notRedProcess")
	public ModelAndView notRedProcess() {
		ModelAndView mv = new ModelAndView("desmartportal/not_read");
		return mv;
	}
	
	
	@RequestMapping("draftsInfo")
	public ModelAndView draftsInfo(@RequestParam(value = "proUid",required = false) String proUid,
			@RequestParam(value = "proAppId",required = false) String proAppId, @RequestParam(value = "verUid",required = false) String verUid,
			@RequestParam(value = "dfsId",required = false) String dfsId) {
		ModelAndView mv = new ModelAndView("desmartportal/drafts_info");
		mv.addObject("proUid",proUid);
		mv.addObject("proAppId",proAppId);
		mv.addObject("verUid",verUid);
		mv.addObject("dfsId",dfsId);
		// 查询草稿数据
		DhDrafts drafts = dhDraftsService.selectBydfsId(dfsId);
		String dfsdata = drafts.getDfsData();
		mv.addObject("dfsData", dfsdata);
		System.err.println(dfsdata);
		// 表单详细信息设置
		Map<String,Object> resultMap = dhProcessFormService.queryProcessForm(proAppId, proUid, verUid);
		mv.addObject("formId", resultMap.get("formId"));
		return mv;
	}
}
