package com.desmart.desmartbpm.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhOperLog;
import com.desmart.desmartbpm.service.OperLogService;

@Controller
@RequestMapping(value="/operLog")
public class OperLogController {
	
	private static final Logger log = LoggerFactory.getLogger(OperLogController.class);
	
	@Resource
	private OperLogService operLogService;
			
	@RequestMapping(value="/index")
	public String index() {
		
		return "desmartbpm/operLog";
	}
	
	@RequestMapping("/pageOperLogList")
	@ResponseBody
	public ServerResponse pageOperLogList(DhOperLog dhOperLog,Integer pageNum,Integer pageSize){	
		return operLogService.pageOperLogByCondition(dhOperLog, pageNum, pageSize);
	}

}
