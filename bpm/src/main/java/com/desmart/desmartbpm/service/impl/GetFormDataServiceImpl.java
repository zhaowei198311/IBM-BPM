package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.CommonBusinessObjectUtils;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.GetFormDataService;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

@Service
public class GetFormDataServiceImpl implements GetFormDataService {
	
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	
	@Autowired
	private DhRouteService dhRouteService;
	
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	
	@Override
	public ServerResponse<?> insertFormData(Map<String, Object> map) {
		String proUid = "25.52a983be-caf0-4666-bfac-cccf92704bca";
		String proAppId = "2066.9c66d06c-dd8d-4fee-b351-78635d532a83";
		String insBusinessKey = "default";
		try {
			// processData
			JSONObject processData = new JSONObject();
			processData.put("departNo", map.get("departNo"));
			processData.put("companyNumber", map.get("companyNumber"));
			processData.put("insTitle", map.get("insTitle"));
			processData.put("insInitUser", map.get("insInitUser"));
			// formData "formData":{"txt2":{"value":""},"price":{"value":"100"}}
			JSONObject formData = JSONObject.parseObject(map.get("formData").toString()) ;
			Iterator<Entry<String, Object>> iterator = formData.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				JSONObject value = new JSONObject();
				value.put("value", entry.getValue());
				formData.put(entry.getKey(), value);
			}
			
			// DH_PROCESS_INSTANCE
			DhProcessDefinition processDefintion = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
			DhProcessInstance dhProcessInstance = null;
//			if (processDefintion == null) {
//				return ServerResponse.createByErrorMessage("当前流程没有可发起的版本");
//			}
//			if(checkPermissionStart(processDefintion)) {
			dhProcessInstance = dhProcessInstanceService.generateDraftProcessInstance(processDefintion, insBusinessKey);
			String proVerUid = dhProcessInstance.getProVerUid();
			JSONObject $insData = JSONObject.parseObject(dhProcessInstance.getInsData());
			String insUid = $insData.getJSONObject("processData").get("insUid").toString();
			processData.put("insUid", insUid);
//			}else {
//				return ServerResponse.createByErrorMessage("无权限发起当前流程");
//			}	
			JSONObject insData = new JSONObject();
			insData.put("processData", processData);
			insData.put("formData", formData);
			dhProcessInstance.setInsInitUser(map.get("insInitUser").toString());
			dhProcessInstance.setInsData(insData.toJSONString());
			dhProcessInstanceMapper.insertProcess(dhProcessInstance);
			
			 // 获得主流程开始节点
	        BpmActivityMeta startNodeOfMainProcess = bpmActivityMetaService.getStartMetaOfMainProcess(proAppId, proUid, proVerUid);

	        // 获得开始节点往后的路由信息
	        BpmRoutingData routingDataOfMainStartNode = dhRouteService.getRoutingDataOfNextActivityTo(startNodeOfMainProcess, new JSONObject());

	        // 获得第一个人工节点
	        BpmActivityMeta firstHumanActivity = routingDataOfMainStartNode.getNormalNodes().iterator().next();
			// 传递第一个环节处理人信息
			CommonBusinessObject pubBo = new CommonBusinessObject();
			String firstUserVarname = firstHumanActivity.getDhActivityConf().getActcAssignVariable();
			List<String> creatorIdList = new ArrayList<>();
			creatorIdList.add((String)map.get("insInitUser"));
			CommonBusinessObjectUtils.setNextOwners(firstUserVarname, pubBo, creatorIdList);

			// 调用API 发起一个流程
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
	        HttpReturnStatus result = bpmProcessUtil.startProcess(proAppId, proUid, proVerUid, pubBo);
	        
	        if (!BpmClientUtils.isErrorResult(result)) {
	        	JSONObject startProcessDataJson = JSON.parseObject(result.getMsg());
	            int insId = Integer.parseInt(startProcessDataJson.getJSONObject("data").getString("piid"));
	            
	            DhProcessInstance $dhProcessInstance = new DhProcessInstance();
	            $dhProcessInstance.setInsUid(dhProcessInstance.getInsUid());
	            $dhProcessInstance.setInsId(insId);
	            $dhProcessInstance.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
	            dhProcessInstanceMapper.updateByPrimaryKeySelective($dhProcessInstance);
	        	return ServerResponse.createBySuccess();
	        }
	        return ServerResponse.createByErrorMessage("发起失败");
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
		
	}
	
}
