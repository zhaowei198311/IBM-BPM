package com.desmart.desmartbpm.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.annotation.log.Log;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.DhTurnTaskRecord;
import com.desmart.desmartbpm.service.DhTaskInstanceTurnService;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhTaskInstanceService;
@Controller
@RequestMapping("/dhTaskInstanceTurn")
public class DhTaskInstanceTurnController {
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceServiceImpl;
	@Autowired
	private DhTaskInstanceTurnService dhTaskInstanceTurnServiceImpl;

	@RequestMapping("/turnTaskInstance")
	public ModelAndView turnTaskInstance() {
		String userId = SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER).toString();
		String userName = SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER_NAME).toString();
		ModelAndView modelAndView = new ModelAndView("desmartbpm/turnTaskInstance");
		modelAndView.addObject("userId", userId);
		modelAndView.addObject("userName",userName);
		return modelAndView;
	}
	
	@RequestMapping("/transferTaskInstance")
	public ModelAndView transferTaskInstance() {
		String userId = SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER).toString();
		String userName = SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER_NAME).toString();
		ModelAndView modelAndView = new ModelAndView("desmartbpm/transferTaskInstance");
		modelAndView.addObject("userId", userId);
		modelAndView.addObject("userName",userName);
		return modelAndView;
	}
	
	@RequestMapping("/batchTurnTaskInstanceByUser")
	@Log(description="批量移交用户任务")
	@ResponseBody
	public ServerResponse batchTurnTaskInstanceByUser(@RequestParam("dhTaskUidList")List<String> dhTaskUidList
				,DhTurnTaskRecord dhTurnTaskRecord) {
		
		return dhTaskInstanceTurnServiceImpl.batchTurnTaskInstanceByUser(dhTaskUidList,dhTurnTaskRecord);
	}
	@RequestMapping("/allTurnTaskInstanceByUser")
	@Log(description="移交用户所有任务")
	@ResponseBody
	public ServerResponse allTurnTaskInstanceByUser(DhTurnTaskRecord dhTurnTaskRecord) {
		
		return dhTaskInstanceTurnServiceImpl.allTurnTaskInstanceByUser(dhTurnTaskRecord);
	}
	@RequestMapping("/getTaskInstanceByPage")
	@ResponseBody
	public ServerResponse getTaskInstanceByPage(DhTaskInstance dhTaskInstance,Integer pageSize
			,Integer pageNum,String isAgent) {
		return dhTaskInstanceServiceImpl
				.selectBackLogTaskInfoByCondition(null, null, dhTaskInstance, pageNum, pageSize, isAgent);
	}
	@RequestMapping("/getClosedTaskInstanceByPage")
	@ResponseBody
	public ServerResponse getClosedTaskInstanceByPage(DhTaskInstance dhTaskInstance,Integer pageSize
			,Integer pageNum,String isAgent,String proName,@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date endTime) {
		if((proName!=null&& !"".equals(proName))) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        	dhProcessInstance.setProName(proName);
        	dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        }
		return dhTaskInstanceServiceImpl.loadPageTaskByClosedByCondition(startTime, endTime
				, dhTaskInstance, pageNum, pageSize, isAgent);
	}
	@RequestMapping("/batchTransferTaskInstanceByUser")
	@Log(description="批量抄送用户任务")
	@ResponseBody
	public ServerResponse batchTransferTaskInstanceByUser(@RequestParam("dhTaskUidList")List<String> dhTaskUidList
				,DhTurnTaskRecord dhTurnTaskRecord) {
		
		return dhTaskInstanceTurnServiceImpl.batchTransferTaskInstanceByUser(dhTaskUidList,dhTurnTaskRecord);
	}
	@RequestMapping("/allTransferTaskInstanceByUser")
	/*@Log(description="抄送用户所有任务")*/
	@ResponseBody
	public ServerResponse allTransferTaskInstanceByUser(DhTurnTaskRecord dhTurnTaskRecord,String isAgent,String proName
			,@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date endTime) {
		DhTaskInstance dhTaskInstance = new DhTaskInstance();
		dhTaskInstance.setUsrUid(dhTurnTaskRecord.getSourceUserUid());
		if((proName!=null&& !"".equals(proName))) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        	dhProcessInstance.setProName(proName);
        	dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        }
		return dhTaskInstanceTurnServiceImpl.allTransferTaskInstanceByUser(dhTurnTaskRecord,dhTaskInstance,isAgent,startTime,endTime);
	}
}
