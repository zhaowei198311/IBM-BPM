package com.desmart.desmartportal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhRouteService;

public class DhRouteServiceImpl implements DhRouteService {
	
	
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	

	@Override
	public ServerResponse<List<BpmActivityMeta>> showRouteBar(String insUid, String activityId, String departNo,
			String companyNum, String formData) {
		// TODO Auto-generated method stub
		//根据表单字段查
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		String insInitUser=dhProcessInstance.getInsInitUser(); //流程发起人
		
		dhProcessInstance.getInsData();
		
		
		return null;
	}

	@Override
	public List<BpmActivityMeta> getNextActivities(BpmActivityMeta sourceActivityMeta, JSONObject formData) {
	    Map<String, Object> nextToResultMap = bpmActivityMetaService.getNextToActivity(sourceActivityMeta, "");
	    
	    
	    
	    return null;
	}
	
}
