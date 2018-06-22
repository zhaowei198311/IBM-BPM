package com.desmart.desmartbpm.service;

import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
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
	ServerResponse queryFieldByFormIdAndStepId(String stepUid, String formUid,String fieldType);

	/**
	 * 保存某步骤上表单字段的权限信息
	 */
	ServerResponse saveFormFieldPermission(DhObjectPermission[] dhObjectPermissions);
	
	/**
	 * 获得某步骤上代办的表单字段的权限信息(返回的是封装了字段权限的json数据)
	 */
	ServerResponse<String> queryFieldPermissionByStepUid(String stepUid);
	
	/**
	 * 获得某步骤上已办的表单字段权限信息(返回的是封装了字段权限的json数据)
	 */
	ServerResponse<String> queryFinshedFieldPerMissionByStepUid(String stepUid);
	
	/**
	 * 通过表单ID获得所有字段
	 */
	ServerResponse<List<BpmFormField>> queryFieldByFormUid(String formUid);

	/**
	 * 根据表单id获得该表单中所有的动态表格
	 */
	ServerResponse<List<BpmFormField>> queryFormTabByFormUid(String formUid);

	/**
	 * 根据表单id和表格名获得表单中某个表格的字段
	 */
	ServerResponse<List<BpmFormField>> queryFormTabFieldByFormUidAndTabName(String formUid, String tableName);
}
