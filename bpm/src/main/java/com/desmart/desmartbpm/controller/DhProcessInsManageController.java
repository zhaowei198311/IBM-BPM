package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.service.DhProcessInsManageService;
import com.desmart.desmartportal.entity.DhProcessInstance;

/**
 * 流程实例管理控制器
 * @author loser_wu
 * @since 2018年5月22日
 */
@Controller
@RequestMapping("/dhProcessInsManage")
public class DhProcessInsManageController {
	@Autowired
	private DhProcessInsManageService dhProcessInstanceService;
	
	@RequestMapping("/toProcessInsManage")
	public String toProcessInsManage() {
		return "desmartbpm/processInstance";
	}
	
	@RequestMapping("/getProcesssInstance")
	@ResponseBody
	public ServerResponse getProcesssInstance(Integer pageNum,Integer pageSize
				,DhProcessInstance dhProcessInstance) {
		return dhProcessInstanceService.getProcesssInstanceListByCondition(pageNum, pageSize, dhProcessInstance);
	}
	
	@RequestMapping("/terminateProcessIns")
	@ResponseBody
	public ServerResponse terminateProcessIns(DhProcessInstance dhProcessInstance) {
		return dhProcessInstanceService.terminateProcessIns(dhProcessInstance);
	}
	@RequestMapping("/pauseProcessIns")
	@ResponseBody
	public ServerResponse pauseProcessIns(DhProcessInstance dhProcessInstance) {
		return dhProcessInstanceService.pauseProcessIns(dhProcessInstance);
	}
	@RequestMapping("/resumeProcessIns")
	@ResponseBody
	public ServerResponse resumeProcessIns(DhProcessInstance dhProcessInstance) {
		return dhProcessInstanceService.resumeProcessIns(dhProcessInstance);
	}
	@RequestMapping("/getProcessInsInfo")
	@ResponseBody
	public ServerResponse getProcessInsInfo(DhProcessInstance dhProcessInstance) {
		return dhProcessInstanceService.getProcessInsInfo(dhProcessInstance);
	}
	@RequestMapping("/trunOffProcessIns")
	@ResponseBody
	public ServerResponse trunOffProcessIns(DhProcessInstance dhProcessInstance) {
		return dhProcessInstanceService.trunOffProcessIns(dhProcessInstance);
	}
}
