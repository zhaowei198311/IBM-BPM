package com.desmart.desmartportal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.util.JsonUtil;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartportal.service.DhTaskInstanceService;

@Controller
@RequestMapping("/dhRoutingRecord")
public class DhRoutingRecordController {

	@Autowired
	private DhRoutingRecordService dhRoutingRecordServiceImpl;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;
	@Autowired
	private DhProcessInstanceService dhProcessInstanceServiceImpl;
	@Autowired
	private DhTaskInstanceService dhTaskInstanceServiceImpl;
	
	@RequestMapping("/loadDhRoutingRecords.do")
	@ResponseBody
	public ServerResponse loadDhRoutingRecords(@RequestParam String insUid
			,@RequestParam Integer insId
			,@RequestParam String proVerUid
			,@RequestParam String proAppId
			,@RequestParam String proUid
			,@RequestParam(value="bpmActivityList",required=false) String bpmActivityList) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setInsUid(insUid);
		List<DhRoutingRecord> dhRoutingRecords = dhRoutingRecordServiceImpl.getDhRoutingRecordListByCondition(dhRoutingRecord);
		List<DhTaskInstance> dhTaskInstances = dhTaskInstanceServiceImpl.selectByInsUidAndTaskTypeCondition(insUid);
		List<BpmActivityMeta> bpmActivityMetas = new ArrayList<BpmActivityMeta>();
		if(bpmActivityList!=null&&!"".equals(bpmActivityList)) {
			bpmActivityMetas = JsonUtil.string2Obj(bpmActivityList, ArrayList.class, BpmActivityMeta.class);
		}
		List<BpmActivityMeta> bpmActivityMetaList = new ArrayList<BpmActivityMeta>();
		for (DhTaskInstance record : dhTaskInstances) {
			if(DhTaskInstance.STATUS_RECEIVED.equals(record.getTaskStatus()))//循环确定该流程实例任务中，有哪些是环节处于接收到任务
			{				
				String activity_bpd_id = record.getActivityBpdId();
				for (BpmActivityMeta bpmActivityMeta : bpmActivityMetas) {
					if(activity_bpd_id.equals(bpmActivityMeta.getActivityBpdId())//锁定到一个环节
							&&proUid.equals(bpmActivityMeta.getBpdId())
							&&proVerUid.equals(bpmActivityMeta.getSnapshotId())&&proAppId.equals(bpmActivityMeta.getProAppId())) {
					bpmActivityMetaList.add(bpmActivityMeta);
				}
			}
			}
		}
		//BpmActivityMeta bpmActivityMeta = bpmActivityMetaServiceImpl.queryByPrimaryKey(activityId);
	    
		List<DhTaskHandler> dhTaskHandlers = new ArrayList<DhTaskHandler>();
		for (BpmActivityMeta bpmActivityMeta2 : bpmActivityMetaList) {
			List<DhTaskHandler> dhTaskHandlers1 = dhRoutingRecordServiceImpl.getListByInsIdAndActivityBpdId(insId, bpmActivityMeta2.getActivityBpdId());
			if(dhTaskHandlers1!=null) {
				for (DhTaskHandler dhTaskHandler : dhTaskHandlers1) {
					dhTaskHandlers.add(dhTaskHandler);
				}
			}
		}
		
		Map<String, Object> data = new HashMap<String,Object>();
		
		data.put("bpmActivityMetaList", bpmActivityMetaList);
		data.put("dhRoutingRecords", dhRoutingRecords);
		data.put("dhTaskHandlers", dhTaskHandlers);
		return ServerResponse.createBySuccess(data);
	}
}
