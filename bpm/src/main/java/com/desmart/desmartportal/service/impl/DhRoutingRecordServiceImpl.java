package com.desmart.desmartportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.DhTaskHandlerMapper;
import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhRoutingRecordService;
@Service
public class DhRoutingRecordServiceImpl implements DhRoutingRecordService {

	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
	@Autowired
	private DhTaskHandlerMapper dhTaskHandlerMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Override
	public List<DhRoutingRecord> getDhRoutingRecordListByCondition(DhRoutingRecord dhRoutingRecord) {
		// TODO Auto-generated method stub
		return dhRoutingRecordMapper.getDhRoutingRecordListByCondition(dhRoutingRecord);
	}

	@Override
	public List<DhTaskHandler> getListByInsIdAndActivityBpdId(Integer insId, String activityBpdId) {
		// TODO Auto-generated method stub
		return dhTaskHandlerMapper.getListByInsIdAndActivityBpdId(insId, activityBpdId);
	}


}
