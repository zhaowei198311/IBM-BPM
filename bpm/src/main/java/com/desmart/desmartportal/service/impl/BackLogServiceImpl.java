package com.desmart.desmartportal.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.dao.BackLogMapper;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.BackLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class BackLogServiceImpl implements BackLogService {

	@Autowired
	private BackLogMapper backLogMapper;
	
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectBackLogTaskInfoByCondition(Date startTime, Date endTime,
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<DhTaskInstance> resultList = backLogMapper.selectBackLogTaskInfoByCondition(startTime, endTime, dhTaskInstance);
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

}
