package com.desmart.desmartportal.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.entity.BpmRoutingData;
import org.apache.shiro.SecurityUtils;
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
    private BpmActivityMetaService bpmActivityMetaService;
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


	public ServerResponse saveSubmitTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance, BpmRoutingData bpmRoutingData) {
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

        // 接下来的额人员环节
        Set<BpmActivityMeta> normalNodes = bpmRoutingData.getNormalNodes();
        Iterator<BpmActivityMeta> it = normalNodes.iterator();
        if (it.hasNext()) {
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

        int insertCount = dhRoutingRecordMapper.insert(dhRoutingRecord);
        if (insertCount > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("保存路由记录失败");
        }

    }

}
