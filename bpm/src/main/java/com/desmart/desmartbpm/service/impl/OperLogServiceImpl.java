package com.desmart.desmartbpm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.OperLogMapper;
import com.desmart.desmartbpm.entity.DhOperLog;
import com.desmart.desmartbpm.service.OperLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class OperLogServiceImpl implements OperLogService{

	@Resource
	private OperLogMapper logMapper;
	
	@Override
	public int save(DhOperLog operLog) {
		int result = logMapper.save(operLog);
		return result;
	}

	@Override
	public ServerResponse<PageInfo<List<DhOperLog>>> pageOperLogByCondition(DhOperLog dhOperLog,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("create_time DESC");
		List<DhOperLog> resultList = logMapper
				.getOperaLogListByCondition(dhOperLog);
		PageInfo<List<DhOperLog>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

}
