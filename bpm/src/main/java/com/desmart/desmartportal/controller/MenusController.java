/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.enums.DhActivityAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.service.DhDraftsService;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.MenusService;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.util.ArrayUtil;

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
	private MenusService menusService;
	
	
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	
	@Autowired
	private DhDraftsService dhDraftsService;
	
	 @Autowired 
     DhActivityAssignMapper dhActivityAssignMapper;
	 
	 @Autowired
     SysRoleUserMapper sysRoleUserMapper;
	 
	 @Autowired
	 SysUserMapper sysUserMapper;
	 
	 @Autowired
	 SysTeamMemberMapper sysTeamMemberMapper;
	 

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
		
		//环节选人
		mv.addObject("activityMetaList", menusService.activityHandler(proUid, proAppId, verUid));
		
		
		// 表单详细信息设置
		Map<String,Object> resultMap = dhProcessFormService.queryProcessForm(proAppId, proUid, verUid);
		mv.addObject("formId", resultMap.get("formId"));
		mv.addObject("actcUid", resultMap.get("actcUid"));
		mv.addObject("activityId", resultMap.get("activityId"));
		mv.addObject("activityBpdId", resultMap.get("activityBpdId"));
		return mv;
	}
	
	
	//可选处理人获取
	@RequestMapping("/choosableHandler")
	@ResponseBody
	public List<SysUser> choosableHandler(String activityUid){
		return menusService.choosableHandler(activityUid);
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
	
	@RequestMapping("approval")
	public ModelAndView approval(@RequestParam(value="taskUid") String taskUid) {
		ModelAndView mv = new ModelAndView("desmartportal/approval");
		Map<String,Object> resultMap = dhTaskInstanceService.taskInfo(taskUid);
		mv.addAllObjects(resultMap);
		return mv;
	}
	
	@RequestMapping("viewProcessImage")
	public ModelAndView viewProcessImage(@RequestParam(value="insId") String insId) {
		ModelAndView mv = new ModelAndView("desmartportal/viewProcessImage");
		mv.addObject("insId",insId);
		return mv;
	}
}
