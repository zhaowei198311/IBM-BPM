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

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.service.DhDraftsService;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.MenusService;
import com.desmart.desmartportal.service.UserProcessService;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserDepartmentMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.SysUserDepartmentService;

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
	private MenusService menusService;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private DhDraftsService dhDraftsService;

	@Autowired
	private UserProcessService userProcessService;
	
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	
	@Autowired
	private DhStepService dhStepService;
	
	@Autowired
	private BpmFormManageService bpmFormManageService;
	
	@Autowired
	private SysUserDepartmentService sysUserDepartmentService;
	
	
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
	 * 新建一个流程页面，进入发起流程的页面
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
	    
	    String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		SysUser currentUser = sysUserMapper.queryByPrimaryKey(currentUserUid);
		mv.addObject("currentUser", currentUser);
		
		// 获得发起人部门信息和公司编码信息
        SysUserDepartment sysUserDepartment = new SysUserDepartment();
        sysUserDepartment.setUserUid(currentUserUid);
        List<SysUserDepartment> userDepartmentList = sysUserDepartmentService.selectAll(sysUserDepartment);
		mv.addObject("userDepartmentList", userDepartmentList);
		
		DhProcessDefinition processDefintion = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
		if (processDefintion == null) {
		    mv.setViewName("/desmartbpm/error");
		    mv.addObject("errorMessage", "找不到可供发起的流程");
		}
		mv.addObject("processDefinition", processDefintion);
		
		ServerResponse<BpmActivityMeta> metaResponse = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId, proUid, processDefintion.getProVerUid());
		if (!metaResponse.isSuccess()) {
		    mv.setViewName("/desmartbpm/error");
            mv.addObject("errorMessage", "找不到第一个人工环节");
		}
		BpmActivityMeta firstHumanMeta = metaResponse.getData();
		mv.addObject("bpmActivityMeta", firstHumanMeta);
		mv.addObject("dhActivityConf", firstHumanMeta.getDhActivityConf());
		// 获得默认步骤的列表
		List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(firstHumanMeta, "default");
		
		DhStep formStep = getFirstFormStepOfStepList(steps);
		mv.addObject("dhStep", formStep);
		
		// 获得表单文件内容
		ServerResponse formResponse = bpmFormManageService.getFormFileByFormUid(formStep.getStepObjectUid());
		if (!formResponse.isSuccess()) {
		    mv.setViewName("/desmartbpm/error");
            mv.addObject("errorMessage", "获得表单数据失败");
		}
		mv.addObject("formHtml", formResponse.getData());
		
		// 获得下个环节处理人信息
		mv.addObject("activityMetaList", menusService.activityHandler(proUid, proAppId, verUid));
		
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
		resultMap.put("taskUid", taskUid);
		mv.addAllObjects(resultMap);
		return mv;
	}
	
	@RequestMapping("viewProcessImage")
	public ModelAndView viewProcessImage(@RequestParam(value="insId") String insId) {
		ModelAndView mv = new ModelAndView("desmartportal/viewProcessImage");
		mv.addObject("insId",insId);
		return mv;
	}
	
	private DhStep getFirstFormStepOfStepList(List<DhStep> stepList) {
	    for (DhStep dhStep : stepList) {
	        if (DhStep.TYPE_FORM.equals(dhStep.getStepType())) {
	            return dhStep;
	        }
	    }
	    return null;
	}
}
