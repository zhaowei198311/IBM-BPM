package com.desmart.desmartportal.controller;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.BackLogService;
import com.desmart.desmartsystem.entity.SysUser;

@Controller
@RequestMapping("/backLog")
public class BackLogController {

	@Autowired
	private BackLogService backLogService;
	@RequestMapping("/loadBackLog")
	@ResponseBody
	public ServerResponse loadBackLog(@DateTimeFormat(pattern ="yyyy-MM-dd")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd")Date endTime,
			DhTaskInstance dhTaskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@RequestParam("insTitle")String insTitle,@RequestParam("createProcessUserName")String createProcessUserName) {
		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhTaskInstance.setUsrUid(currentUserUid);
        if(insTitle!=null && !"".equals(insTitle)) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        		dhProcessInstance.setInsTitle(insTitle);
        		dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        	}
        	if(createProcessUserName!=null && !"".equals(createProcessUserName)) {
        		SysUser sysUser = new SysUser();
        		sysUser.setUserName(createProcessUserName);
        		dhTaskInstance.setSysUser(sysUser);
        	}
		return backLogService.selectBackLogTaskInfoByCondition(startTime
				, endTime, dhTaskInstance, pageNum, pageSize);
	}
	
	@RequestMapping("/todoTask")
	@ResponseBody
	public Integer todoTask(String userId) {
		if(userId==null) {
			userId  = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		}
		return backLogService.selectBackLogByusrUid(userId);
	}
}
