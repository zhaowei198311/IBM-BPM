package com.desmart.desmartportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.service.BpmFormManageService;

@RequestMapping(value = "/formSet")
@Controller
public class FormSetController {
	@Autowired
	private BpmFormManageService bpmFormManageService;
	
	/**
	 * 根据表单Id获得表单文件
	 */
	@RequestMapping(value = "/getFormFileByFormUid")
	@ResponseBody
	public ServerResponse getFormFileByFormUid(String dynUid) {
		try {
			return bpmFormManageService.getFormFileByFormUid(dynUid);
		}catch (Exception e) {
			return ServerResponse.createByErrorMessage("获取表单文件失败");
		}
	}
}
