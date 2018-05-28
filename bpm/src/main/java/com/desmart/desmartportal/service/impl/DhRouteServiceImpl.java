package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhGatewayLineService;
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
	@Autowired
	private DhGatewayLineService dhGatewayLineService;

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
	    List<BpmActivityMeta> result = new ArrayList<>();
	    Map<String, Object> resultMap = bpmActivityMetaService.getNextToActivity(sourceActivityMeta, "");
	    List<BpmActivityMeta> normalList = (List<BpmActivityMeta>)resultMap.get("normal");
        List<BpmActivityMeta> gateAndlList = (List<BpmActivityMeta>)resultMap.get("gateAnd");
        List<BpmActivityMeta> endList = (List<BpmActivityMeta>)resultMap.get("end");
        List<BpmActivityMeta> gatewayList = (List<BpmActivityMeta>)resultMap.get("gateway");
	    
        result.addAll(normalList);
        result.addAll(gateAndlList);
        // 查看是否需要校验排他网关
        if (gatewayList.isEmpty()) {
            return result;
        }
        
        // 被排他网关排除的节点
        List<String> activityIdsToRemove = new ArrayList<>();
        
        for (BpmActivityMeta gatewayMeta : gatewayList) {
            DhGatewayLine lineSelective = new DhGatewayLine();
            lineSelective.setActivityId(gatewayMeta.getActivityId());
            List<DhGatewayLine> lines = dhGatewayLineService.getGateWayLinesByCondition(lineSelective);
            // 找出这个规则需要的所有表单字段
            // List<String> variableNeeded
            // formData.getString(var)
            // 
            
            
        }
	    
	    return result;
	}
	
}
