package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;

public interface BpmFormFieldService {

	/**
	 * 保存表单字段数据
	 */
	ServerResponse saveFormField(BpmFormField[] fields);

	/**
	 * 通过表单ID获得所属字段信息及权限信息
	 */
	ServerResponse queryFieldByFormIdAndStepId(String stepUid, String formUid);

	/**
	 * 保存某环节上表单字段的权限信息
	 */
	ServerResponse saveFormFieldPermission(DhObjectPermission[] dhObjectPermissions);
}
