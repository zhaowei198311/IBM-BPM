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
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
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
	private DhTaskInstanceService dhTaskInstanceServiceImpl;
	
	@RequestMapping("/loadDhRoutingRecords.do")
	@ResponseBody
	public ServerResponse loadDhRoutingRecords(@RequestParam String insUid
			,@RequestParam Integer insId) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setInsUid(insUid);
		List<DhRoutingRecord> dhRoutingRecords = dhRoutingRecordServiceImpl.getDhRoutingRecordListByCondition(dhRoutingRecord);

		List<BpmActivityMeta> bpmActivityMetaList = new ArrayList<BpmActivityMeta>();//获取当前流转到的所有环节


		if (bpmActivityMetaList.size() > 0) {
			DhRoutingRecord lastDhRoutingRecord = dhRoutingRecords.get(dhRoutingRecords.size()-1);//根据流程uid取得最后一个流转记录
			if(lastDhRoutingRecord.getActivityTo()!=null&&!"".equals(lastDhRoutingRecord.getActivityTo())) {
				String[] activityTo = lastDhRoutingRecord.getActivityTo().split(",");
				for (int i = 0; i < activityTo.length; i++) {
					String activityId = activityTo[i];
					BpmActivityMeta bpmActivityMeta = bpmActivityMetaServiceImpl.queryByPrimaryKey(activityId);
					bpmActivityMetaList.add(bpmActivityMeta);
				}
			}
		}
		
		/*List<DhTaskInstance> dhTaskHandlers = new ArrayList<DhTaskInstance>();//获得当前要处理的任务的信息
		for (BpmActivityMeta bpmActivityMeta : bpmActivityMetaList) {
			String activity_bpd_id = bpmActivityMeta.getActivityBpdId();
			if(activity_bpd_id!=null&&!"".equals(activity_bpd_id)) {
			DhTaskInstance dhTaskInstanceSelect = new DhTaskInstance();
			dhTaskInstanceSelect.setInsUid(insUid);
			dhTaskInstanceSelect.setActivityBpdId(activity_bpd_id);
			List<DhTaskInstance> dhTaskInstances = dhTaskInstanceServiceImpl.selectByCondition(dhTaskInstanceSelect);
			if(dhRoutingRecords!=null&&dhRoutingRecords.size()>0) {
				dhTaskHandlers.add(dhTaskInstances.get(0));
			}
			}
		}*/
		
		DhTaskInstance dhTaskInstanceSelect = new DhTaskInstance();
		dhTaskInstanceSelect.setInsUid(insUid);
		List<DhTaskInstance> dhTaskInstances = dhTaskInstanceServiceImpl.selectByCondition(dhTaskInstanceSelect);
		
		List<DhTaskInstance> dhTaskHandlers = new ArrayList<DhTaskInstance>();//获得当前要处理的任务的信息
		for (BpmActivityMeta bpmActivityMeta : bpmActivityMetaList) {
			String activity_bpd_id = bpmActivityMeta.getActivityBpdId();
		for (DhTaskInstance dhTaskInstance : dhTaskInstances) {
				if(bpmActivityMeta.getActivityBpdId().equals(activity_bpd_id)) {
					if(DhTaskInstance.STATUS_RECEIVED.equals(dhTaskInstance.getTaskStatus())){
					dhTaskHandlers.add(dhTaskInstance);
					}
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
