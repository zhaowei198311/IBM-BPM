package com.desmart.desmartbpm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class BpmFormManageServiceImpl implements BpmFormManageService{
	@Autowired
	private BpmFormManageMapper bpmFormManageMapper;
	
	@Override
	public ServerResponse<PageInfo<List<BpmForm>>> listFormByProcessCategory(List<DhProcessDefinition> dhProcessList, String formTitle, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		//获得的数据
		List<BpmForm> formList = bpmFormManageMapper.listFormByProcessCategory(dhProcessList,formTitle);
		PageInfo<List<BpmForm>> pageInfo = new PageInfo(formList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse listFormByProDefinition(String formTitle, String proUid, String proVerUid, Integer pageNum,
			Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		//获得的数据
		List<BpmForm> formList = bpmFormManageMapper.listFormByProDefinition(formTitle,proUid,proVerUid);
		PageInfo<List<BpmForm>> pageInfo = new PageInfo(formList);
		return ServerResponse.createBySuccess(pageInfo);
	}

}
