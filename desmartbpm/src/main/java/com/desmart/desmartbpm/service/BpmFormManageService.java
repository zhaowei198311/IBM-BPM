package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

public interface BpmFormManageService {
	/**
	 * 获得某版本的流程下的所有表单
	 */
	ServerResponse listFormByProDefinition(String formTitle, String proUid, String proCategoryUid, 
			String proVerUid, Integer pageNum,Integer pageSize);

	/**
	 * 获得所有的流程定义对象(DH)
	 */
	List<DhProcessDefinition> listDefinitionAll();

	/**
	 * 添加表单数据
	 */
	ServerResponse saveForm(BpmForm bpmForm);

	/**
	 * 通过表单名查询表单是否存在
	 */
	ServerResponse queryFormByName(String dynTitle);
}
