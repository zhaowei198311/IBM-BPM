package com.desmart.desmartbpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.service.BpmFormFieldService;

import java.util.List;

@RequestMapping(value = "/formField")
@Controller
public class BpmFormFieldController {
	private static final Logger logger = LoggerFactory.getLogger(BpmFormFieldController.class);

	@Autowired
	private BpmFormFieldService bpmFormFieldService;
	
	/**
	 * 保存表单字段
	 */
	@RequestMapping(value = "/saveFormField")
	@ResponseBody
	public ServerResponse saveFormField(@RequestBody BpmFormField[] fields) {
		try {
			return bpmFormFieldService.saveFormField(fields);
		}catch(Exception e) {
			logger.error("保存表单字段异常", e);
			return ServerResponse.createByError();
		}
	}
	
	/**
	 * 通过表单ID,步骤ID以及字段类型获得所属字段信息及权限信息
	 */
	@RequestMapping(value = "/queryFieldByFormUidAndStepId")
	@ResponseBody
	public ServerResponse queryFieldByFormUidAndStepId(String stepUid,String formUid,String fieldType) {
		try {
			return bpmFormFieldService.queryFieldByFormIdAndStepId(stepUid,formUid,fieldType);
		}catch(Exception e) {
			logger.error("查询字段权限失败", e);
			return ServerResponse.createByError();
		}
	}
	
	/**
	 * 保存某环节上表单字段的权限信息
	 */
	@RequestMapping(value = "/saveFormFieldPermission")
	@ResponseBody
	public ServerResponse saveFormFieldPermission(@RequestBody DhObjectPermission[] dhObjectPermissions) {
		try {
			return bpmFormFieldService.saveFormFieldPermission(dhObjectPermissions);
		}catch(Exception e) {
			logger.error("保存字段权限失败", e);
			return ServerResponse.createByError();
		}
	}
	
	/**
	 * 通过表单ID 查询所有表单字段
	 */
	@RequestMapping(value = "/queryFieldByFromUid")
	@ResponseBody
	public ServerResponse queryFieldByFormUid(String formUid) {
		try {
            return bpmFormFieldService.queryFieldByFormUid(formUid);
		}catch(Exception e) {
			logger.error("获取表单字段异常", e);
			return ServerResponse.createByErrorMessage("获得表单字段失败");
		}
	}
	

	/**
	 * 根据表单id获得该表单中所有的动态表格
	 */
	@RequestMapping(value = "/queryFormTabByFormUid")
	@ResponseBody
	public ServerResponse queryFormTabByFormUid(String formUid) {
		try {
			return bpmFormFieldService.queryFormTabByFormUid(formUid);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
	
	/**
	 * 根据表单id获得该表单中所有的动态表格
	 */
	@RequestMapping(value = "/queryFormTabFieldByFormUidAndTabName")
	@ResponseBody
	public ServerResponse queryFormTabFieldByFormUidAndTabName(String formUid,String tableName) {
		try {
			return bpmFormFieldService.queryFormTabFieldByFormUidAndTabName(formUid,tableName);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
}
