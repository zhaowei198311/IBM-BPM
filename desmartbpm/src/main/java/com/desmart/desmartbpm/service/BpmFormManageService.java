package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

public interface BpmFormManageService {

	/**
	 * 获得所有的表单
	 * @param object 
	 */
	ServerResponse listForm(List<DhProcessDefinition> dhProcessList, String formTitle, Integer pageNum, Integer pageSize);
	
}
