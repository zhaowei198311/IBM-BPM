package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.desmart.desmartbpm.entity.DhProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmFormManageDao;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.BpmFormManageService;

@Service
public class BpmFormManageServiceImpl implements BpmFormManageService{
	@Autowired
	private BpmFormManageDao bpmFormManageDao;
	
	@Override
	public ServerResponse listForm(List<DhProcessDefinition> dhProcessList, String formTitle, Integer pageNum, Integer pageSize) {
		pageNum = pageNum == null ? pageNum : 0;
		pageSize = pageSize == null ? pageSize : Math.abs(pageSize);
		//获得的数据
		List<BpmForm> exposeItemList = bpmFormManageDao.listForm(dhProcessList,formTitle);
		return null;
	}

}
