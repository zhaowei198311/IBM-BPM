package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.service.BpmFormFieldService;

@RequestMapping(value = "/formField")
@Controller
public class BpmFormFieldController {
	@Autowired
	private BpmFormFieldService bpmFormFieldService;
	
	/**
	 * 保存表单字段
	 */
	@RequestMapping(value = "/saveFormField")
	@ResponseBody
	public ServerResponse saveFormField(@RequestBody BpmFormField[] fields) {
		return bpmFormFieldService.saveFormField(fields);
	}
}
