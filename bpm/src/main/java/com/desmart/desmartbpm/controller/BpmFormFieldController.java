package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;
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
	
	/**
	 * 通过表单ID,步骤ID以及字段类型获得所属字段信息及权限信息
	 */
	@RequestMapping(value = "/queryFieldByFormUidAndStepId")
	@ResponseBody
	public ServerResponse queryFieldByFormUidAndStepId(String stepUid,String formUid,String fieldType) {
		return bpmFormFieldService.queryFieldByFormIdAndStepId(stepUid,formUid,fieldType);
	}
	
	/**
	 * 保存某环节上表单字段的权限信息
	 */
	@RequestMapping(value = "/saveFormFieldPermission")
	@ResponseBody
	public ServerResponse saveFormFieldPermission(@RequestBody DhObjectPermission[] dhObjectPermissions) {
		return bpmFormFieldService.saveFormFieldPermission(dhObjectPermissions);
	}
	
	/**
	 * 通过表单ID 查询所有表单字段
	 */
	@RequestMapping(value = "/queryFieldByFromUid")
	@ResponseBody
	public ServerResponse queryFieldByFormUid(String formUid) {
		return bpmFormFieldService.queryFieldByFormUid(formUid);
	}
}
