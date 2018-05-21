package com.desmart.desmartportal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRoutingRecordService;

@Controller
@RequestMapping("/dhRoutingRecord")
public class DhRoutingRecordController {

	@Autowired
	private DhRoutingRecordService dhRoutingRecordServiceImpl;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;
	@Autowired
	private DhProcessInstanceService dhProcessInstanceServiceImpl;
	
	@RequestMapping("/loadDhRoutingRecords.do")
	@ResponseBody
	public ServerResponse loadDhRoutingRecords(String insUid
			,Integer insId,String proAppId,String proUid
			,String proVerUid,String bpdId,String activityBpdId) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setInsUid(insUid);
		List<DhRoutingRecord> dhRoutingRecords = dhRoutingRecordServiceImpl.getDhRoutingRecordListByCondition(dhRoutingRecord);
		
		BpmActivityMeta bpmActivityMeta = bpmActivityMetaServiceImpl.getBpmActivityMeta(proAppId, activityBpdId, proVerUid, bpdId);
		List<DhTaskHandler> dhTaskHandlers = null;
		if(bpmActivityMeta!=null) {
			dhTaskHandlers = dhRoutingRecordServiceImpl.getListByInsIdAndActivityBpdId(insId, bpmActivityMeta.getActivityBpdId());
		}
		Map<String, Object> data = new HashMap<String,Object>();
		
		data.put("dhRoutingRecords", dhRoutingRecords);
		data.put("bpmActivityMeta", bpmActivityMeta);
		data.put("dhTaskHandlers", dhTaskHandlers);
		return ServerResponse.createBySuccess(data);
	}
}
