package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

public interface BpmFormManageService {

	/**
	 * 获得某流程分类下所有流程定义的所有表单
	 */
	ServerResponse listFormByProcessCategory(List<DhProcessDefinition> dhProcessList, String formTitle, Integer pageNum, Integer pageSize);

	/**
	 * 获得某版本的流程下的所有表单
	 */
	ServerResponse listFormByProDefinition(String formTitle, String proUid, String proVerUid, Integer pageNum,
			Integer pageSize);
	
}
