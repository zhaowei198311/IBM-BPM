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
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhProcessService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
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
	private DhProcessService dhProcessService;
	
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	
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
		// 找到当前环节
		BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
		bpmActivityMeta.setBpdId(proUid); 
		bpmActivityMeta.setProAppId(proAppId);
		bpmActivityMeta.setSnapshotId(verUid);
		bpmActivityMeta.setActivityType(BpmActivityMeta.ACTIVITY_TYPE_START);
		
		
		BpmActivityMeta activtyMeta=new BpmActivityMeta();
		List<BpmActivityMeta> resultList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
		for (BpmActivityMeta bpmActivityMeta2 : resultList) {
			activtyMeta=bpmActivityMeta2;
		}
		
		
		//------------------------------------------------下一环节审批人开始
		Map<String, Object> activtyMap= bpmActivityMetaService.getNextToActivity(activtyMeta, "");
    	List<BpmActivityMeta> activityMetaList=new ArrayList<BpmActivityMeta>();
  		List<BpmActivityMeta> normal=(List<BpmActivityMeta>) activtyMap.get("normal");
  		List<BpmActivityMeta> gateAnd=(List<BpmActivityMeta>) activtyMap.get("gateAnd");
  		activityMetaList.addAll(gateAnd);
  		activityMetaList.addAll(normal);
  		//环节配置获取
		for (BpmActivityMeta activityMeta : activityMetaList) {
			DhActivityConf dhActivityConf=activityMeta.getDhActivityConf();
			String actcAssignType = dhActivityConf.getActcAssignType();
	          DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
	          String activityId = dhActivityConf.getActivityId();
	          if (assignTypeEnum == null) {
	        	  System.out.println("处理人类型不符合要求");
	          }
	          if (assignTypeEnum == DhActivityConfAssignType.NONE) {
	              //return ServerResponse.createBySuccess();
	          }
	          DhActivityAssign selective = new DhActivityAssign();
	          selective.setActivityId(activityId);
	          selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode()); 
	          List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
	          if (assignList.size() == 0) {
	              //return ServerResponse.createByErrorMessage("缺少处理人信息");
	        	  System.out.println("缺少处理人信息");
	          }
	          List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList); //角色或部门
	          
	          String userUid="";
	          String userName="";
	          
	          switch (assignTypeEnum) {
	          case ROLE:
	          case ROLE_AND_DEPARTMENT:
	          case ROLE_AND_COMPANY:
	        	  SysRoleUser roleUser= new SysRoleUser();
	        	  roleUser.setRoleUid(ArrayUtil.toArrayString(idList));
	        	  List<SysRoleUser> roleUsers=sysRoleUserMapper.selectByRoleUser(roleUser);
	        	  for (SysRoleUser sysRoleUser : roleUsers) {
	        		  userUid+=sysRoleUser.getUserUid()+";";
	        		  userName+=sysRoleUser.getUserName()+";";
	        	  }
	              break;
	          case TEAM:
	          case TEAM_AND_DEPARTMENT:
	          case TEAM_AND_COMPANY:
	        	  SysTeamMember sysTeamMember=new SysTeamMember();
	        	  sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));
	        	  
	        	  List<SysTeamMember>  sysTeamMembers=sysTeamMemberMapper.selectTeamUser(sysTeamMember);
	        	  for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
	        		  userUid+=sysTeamMember2.getUserUid()+";";
	        		  userName+=sysTeamMember2.getUserName()+";";
				  }
	        	  
	              break;
	          case LEADER_OF_PRE_ACTIVITY_USER:
	              
	              break;
	          case USERS:
	        	  List<SysUser> users=sysUserMapper.listByPrimaryKeyList(idList);
	        	  for (SysUser sysUser : users) {
	        		  userUid+=sysUser.getUserUid()+";";
	        		  userName+=sysUser.getUserName()+";";
	        	  }
	              break;
	          case PROCESS_CREATOR:
	              
	              break;
	          case BY_FIELD:
	              if (assignList.size() > 0) {
	                  String field = assignList.get(0).getActaAssignId();
	                  dhActivityConf.setHandleField(field);
	              }
	              break;
	          default:
	              break; 
	          }
	          activityMeta.setUserUid(userUid);
	          activityMeta.setUserName(userName);
		}
		mv.addObject("activityMetaList", activityMetaList);
		//-------------------------------------------结束
		
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
		List<SysUser> userList=new ArrayList<SysUser>();
		BpmActivityMeta bpmActivityMeta=new BpmActivityMeta();
		bpmActivityMeta.setActivityId(activityUid);
		List<BpmActivityMeta> bpmActivityMetas = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
		
		DhActivityConf dhActivityConf=new DhActivityConf();
		if(bpmActivityMetas!=null){
			dhActivityConf=bpmActivityMetas.get(0).getDhActivityConf();
		}
		
		  String actcAssignType = dhActivityConf.getActcAssignType();
          DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
          String activityId = dhActivityConf.getActivityId();
          if (assignTypeEnum == null) {
        	  System.out.println("处理人类型不符合要求");
        	  return userList;
          }
          if (assignTypeEnum == DhActivityConfAssignType.NONE) {
              //return ServerResponse.createBySuccess();
        	  return userList;
          }
          DhActivityAssign selective = new DhActivityAssign();
          selective.setActivityId(activityId);
          System.err.println(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
          selective.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode()); 
          List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
          if (assignList.size() == 0) {
              //return ServerResponse.createByErrorMessage("缺少处理人信息");
        	  System.out.println("缺少处理人信息");
        	  return userList;
          }
          List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList); //角色或部门
          
          String userUid="";
          String userName="";
          
          switch (assignTypeEnum) {
          case ROLE:
          case ROLE_AND_DEPARTMENT:
          case ROLE_AND_COMPANY:
        	  SysRoleUser roleUser= new SysRoleUser();
        	  roleUser.setRoleUid(ArrayUtil.toArrayString(idList));
        	  List<SysRoleUser> roleUsers=sysRoleUserMapper.selectByRoleUser(roleUser);
        	  for (SysRoleUser sysRoleUser : roleUsers) {
        		  userUid+=sysRoleUser.getUserUid()+";";
        		  userName+=sysRoleUser.getUserName()+";";
        	  }
              break;
          case TEAM:
          case TEAM_AND_DEPARTMENT:
          case TEAM_AND_COMPANY:
        	  SysTeamMember sysTeamMember=new SysTeamMember();
        	  sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));
        	  List<SysTeamMember>  sysTeamMembers=sysTeamMemberMapper.selectTeamUser(sysTeamMember);
        	  for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
        		  userUid+=sysTeamMember2.getUserUid()+";";
        		  userName+=sysTeamMember2.getUserName()+";";
			  }
              break;
          case LEADER_OF_PRE_ACTIVITY_USER:
              
              break;
          case USERS:
        	  List<SysUser> users=sysUserMapper.listByPrimaryKeyList(idList);
        	  for (SysUser sysUser : users) {
        		  userUid+=sysUser.getUserUid();
        		  userName+=sysUser.getUserName();
        	  }
              break;
          case PROCESS_CREATOR:
              
              break;
          case BY_FIELD:
              if (assignList.size() > 0) {
                  String field = assignList.get(0).getActaAssignId();
                  dhActivityConf.setHandleField(field);
              }
              break;
          default:
              break; 
          }
          
          if(StringUtils.isNotBlank(userUid)) {
        	  String [] userUids =	userUid.split(";");
        	  String [] userNames =	userName.split(";");
        	  for (int i = 0; i < userUids.length; i++) {
        		  SysUser sysUser=new SysUser();
        		  sysUser.setUserUid(userUids[i]);
        		  sysUser.setUserName(userNames[i]);
        		  userList.add(sysUser);
			  }
          }
		return userList;
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
	@ResponseBody
	public void viewProcessImage(@RequestParam(value="insId") String insId) {
		dhProcessService.viewProcessImage(insId);
	}
}
