package com.desmart.desmartportal.service.impl;

import java.util.*;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhRoutingRecordService;
@Service
public class DhRoutingRecordServiceImpl implements DhRoutingRecordService {

    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
    @Autowired
    private DhTaskInstanceService taskInstanceService;


	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Override
	public List<DhRoutingRecord> getDhRoutingRecordListByCondition(DhRoutingRecord dhRoutingRecord) {
		// TODO Auto-generated method stub
		return dhRoutingRecordMapper.getDhRoutingRecordListByCondition(dhRoutingRecord);
	}


    @Override
	public ServerResponse<DhRoutingRecord> generateSubmitTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance, BpmRoutingData bpmRoutingData, boolean willTokenMove) {
        BpmActivityMeta taskNode = bpmActivityMetaService.queryByPrimaryKey(taskInstance.getTaskActivityId());

        DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(taskInstance.getInsUid());
		dhRoutingRecord.setActivityName(taskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_Type_SUBMIT_TASK);
		// 路由记录发生人
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(taskInstance.getTaskActivityId());
		String activityTo = null;
        StringBuilder activityToBuilder = new StringBuilder();

        if (willTokenMove) {
            // 如果token要移动的话，处理接下来的人员环节
            Set<BpmActivityMeta> normalNodes = bpmRoutingData.getNormalNodes();
            Iterator<BpmActivityMeta> it = normalNodes.iterator();
            while (it.hasNext()) {
                BpmActivityMeta nextNode = it.next();
                if (nextNode.getParentActivityId().equals(taskNode.getParentActivityId())) {
                    // 说明此环节和当前任务环节在同一个层级
                    activityToBuilder.append(nextNode.getActivityId()).append(",");
                }
            }
            if (activityToBuilder.length() > 0) {
                activityTo = activityToBuilder.toString();
                activityTo = activityTo.substring(0, activityTo.length() - 1);
            }
            dhRoutingRecord.setActivityTo(activityTo);
        }

        return ServerResponse.createBySuccess(dhRoutingRecord);
    }

    @Override
    public DhRoutingRecord generateRejectTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance, BpmActivityMeta targetNode) {
        DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
        dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
        dhRoutingRecord.setInsUid(taskInstance.getInsUid());
        dhRoutingRecord.setActivityName(taskInstance.getTaskTitle());
        dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_Type_REJECT_TASK);
        dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
        dhRoutingRecord.setActivityId(taskInstance.getTaskActivityId());
        String activityTo = targetNode.getActivityId();
        dhRoutingRecord.setActivityTo(activityTo);
        return dhRoutingRecord;
    }

    @Override
    public ServerResponse loadDhRoutingRecords(String insUid) {
        DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
        dhRoutingRecord.setInsUid(insUid);
        List<DhRoutingRecord> dhRoutingRecords = this.getDhRoutingRecordListByCondition(dhRoutingRecord);
        List<BpmActivityMeta> bpmActivityMetaList = new ArrayList<BpmActivityMeta>();//获取当前流转到的所有环节
        List<DhTaskInstance> dhTaskHandlers = new ArrayList<DhTaskInstance>();//获得当前要处理的任务的信息

        DhRoutingRecord lastDhRoutingRecord = getLastRoutingRecordWithActivityTo(dhRoutingRecords);
        if (lastDhRoutingRecord != null) {
            String activityToStr = lastDhRoutingRecord.getActivityTo();
            String[] activityTo = activityToStr.split(",");
            for (int i = 0; i < activityTo.length; i++) {
                String activityId = activityTo[i];
                BpmActivityMeta bpmActivityMeta = bpmActivityMetaService.queryByPrimaryKey(activityId);
                bpmActivityMetaList.add(bpmActivityMeta);
            }

            DhTaskInstance dhTaskInstanceSelect = new DhTaskInstance();
            dhTaskInstanceSelect.setInsUid(insUid);
            // 获得当前流程实例的所有任务
            List<DhTaskInstance> dhTaskInstances = dhTaskInstanceMapper.selectByCondition(dhTaskInstanceSelect);

            // 过滤出当前的任务
            for (DhTaskInstance taskInstance : dhTaskInstances) {
                String taskActivityId = taskInstance.getTaskActivityId();
                String taskStatus = taskInstance.getTaskStatus();
                String taskType = taskInstance.getTaskType();
                String matchedTaskType = DhTaskInstance.TYPE_NORMAL + ";" +DhTaskInstance.TYPE_SIMPLE_LOOP + ";" + DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP;
                if (activityToStr.contains(taskActivityId)
                        && matchedTaskType.contains(taskType)
                        && (taskStatus.equals(DhTaskInstance.STATUS_RECEIVED) || taskStatus.equals(DhTaskInstance.STATUS_WAIT_ALL_ADD_FINISH))) {
                    dhTaskHandlers.add(taskInstance);
                }
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bpmActivityMetaList", bpmActivityMetaList);
        data.put("dhRoutingRecords", dhRoutingRecords);
        data.put("dhTaskHandlers", dhTaskHandlers);
        return ServerResponse.createBySuccess(data);
    }

    /**
     * 从流转记录列表中找到最近的流转记录
     * @param routingRecords 此流程下所有的流转记录
     * @return
     */
    private DhRoutingRecord getLastRoutingRecordWithActivityTo(List<DhRoutingRecord> routingRecords) {
        if (routingRecords == null || routingRecords.isEmpty()) {
            return null;
        }
        for (int i=routingRecords.size() - 1; i>=0; i--) {
            DhRoutingRecord dhRoutingRecord = routingRecords.get(i);
            if (StringUtils.isNotBlank(dhRoutingRecord.getActivityTo())) {
                return dhRoutingRecord;
            }
        }
        return null;
    }

}
