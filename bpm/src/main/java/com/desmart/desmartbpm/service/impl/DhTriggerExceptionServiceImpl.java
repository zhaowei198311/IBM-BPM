package com.desmart.desmartbpm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.DhTriggerExceptionMapper;
import com.desmart.desmartbpm.entity.DhTriggerException;
import com.desmart.desmartbpm.service.DhTriggerExceptionService;

@Service
public class DhTriggerExceptionServiceImpl implements DhTriggerExceptionService {

	@Autowired
	DhTriggerExceptionMapper dhTriggerExceptionMapper;
	
	@Override
	public int save(DhTriggerException dhTriggerException) {
		return dhTriggerExceptionMapper.save(dhTriggerException);
	}

}
