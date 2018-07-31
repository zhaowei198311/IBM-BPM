package com.desmart.desmartbpm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.OperLogMapper;
import com.desmart.desmartbpm.entity.DhOperLog;
import com.desmart.desmartbpm.service.OperLogService;

@Service
public class OperLogServiceImpl implements OperLogService{

	@Resource
	private OperLogMapper logMapper;
	
	@Override
	public int save(DhOperLog operLog) {
		int result = logMapper.save(operLog);
		return result;
	}

}
