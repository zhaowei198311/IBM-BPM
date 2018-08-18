package com.desmart.desmartbpm.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.DhTurnTaskRecordMapper;
import com.desmart.desmartbpm.entity.DhTurnTaskRecord;
import com.desmart.desmartbpm.service.DhTaskInstanceTurnService;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhTaskInstance;

@Service
public class DhTaskInstanceTurnServiceImpl implements DhTaskInstanceTurnService {

	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private DhTurnTaskRecordMapper dhTurnTaskRecordMapper;

	@Override
	@Transactional
	public ServerResponse batchTurnTaskInstanceByUser(List<String> dhTaskUidList, DhTurnTaskRecord dhTurnTaskRecord) {
		List<DhTaskInstance> dhTaskInstances = dhTaskInstanceMapper.batchDhTaskInstancesByPrimaryKey(dhTaskUidList);
		dhTurnTaskRecord.setTurnTaskOperation(DhTurnTaskRecord.OPERATION_TYPE_BATCH);
		dhTurnTaskRecord.setTurnRecordUid(EntityIdPrefix.DH_TURN_TASK_RECORD+UUID.randomUUID());
		StringBuffer turnObjectUid = new StringBuffer();
		for (DhTaskInstance dhTaskInstance : dhTaskInstances) {
			turnObjectUid.append(dhTaskInstance.getTaskUid() + ";");
			if (StringUtils.isNotBlank(dhTaskInstance.getTaskDelegateUser())) {// 若任务代理人是选择的用户，则修改代理人为目标用户
				dhTaskInstance.setTaskDelegateUser(dhTurnTaskRecord.getTargetUserUid());
			} else if (StringUtils.isNotBlank(dhTaskInstance.getUsrUid())) {
				dhTaskInstance.setUsrUid(dhTurnTaskRecord.getTargetUserUid());

			}
		}
		dhTurnTaskRecord.setTurnObjectUid(turnObjectUid.toString());
		Integer count = dhTurnTaskRecordMapper.save(dhTurnTaskRecord);
		if (count != null && count > 0) {
			dhTaskInstanceMapper.turnTaskByBatch(dhTaskInstances);
			return ServerResponse.createBySuccessMessage("任务移交成功");
		} else {
			return ServerResponse.createBySuccessMessage("任务移交失败");
		}
	}

	@Override
	@Transactional
	public ServerResponse allTurnTaskInstanceByUser(DhTurnTaskRecord dhTurnTaskRecord) {
		dhTurnTaskRecord.setTurnRecordUid(EntityIdPrefix.DH_TURN_TASK_RECORD+UUID.randomUUID());
		dhTurnTaskRecord.setTurnTaskOperation(DhTurnTaskRecord.OPERATION_TYPE_ALL);
		dhTurnTaskRecord.setTurnObjectUid(dhTurnTaskRecord.getSourceUserUid());// 移交全部任务则保存原用户的uid
		Integer count = dhTurnTaskRecordMapper.save(dhTurnTaskRecord);
		if (count != null && count > 0) {
			dhTaskInstanceMapper.turnDelegateBackLogTask(dhTurnTaskRecord.getSourceUserUid(),
					dhTurnTaskRecord.getTargetUserUid());// 移交代理人是选择的用户的待办任务
			dhTaskInstanceMapper.turnDisposeBackLogTask(dhTurnTaskRecord.getSourceUserUid(),
					dhTurnTaskRecord.getTargetUserUid());// 移交处理人是选择的用户且没有被代理的待办任务
			return ServerResponse.createBySuccessMessage("任务移交成功");
		} else {
			return ServerResponse.createBySuccessMessage("任务移交失败");
		}
	}

	@Override
	@Transactional
	public ServerResponse batchTransferTaskInstanceByUser(List<String> dhTaskUidList,
			DhTurnTaskRecord dhTurnTaskRecord) {
		List<DhTaskInstance> dhTaskInstances = dhTaskInstanceMapper.batchDhTaskInstancesByPrimaryKey(dhTaskUidList);
		 for(Iterator<DhTaskInstance> iterator = dhTaskInstances.iterator(); iterator.hasNext();)
	     {
			DhTaskInstance dhTaskInstance = iterator.next();
			dhTaskInstance.setTaskType(DhTaskInstance.TYPE_TRANSFER);
			dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
			dhTaskInstance.setFromTaskUid(dhTaskInstance.getTaskUid());
			dhTaskInstance.setTaskInitDate(new Date());
			dhTaskInstance.setTaskDueDate(null);
			dhTaskInstance.setUsrUid(dhTurnTaskRecord.getTargetUserUid());
			DhTaskInstance taskExtis = dhTaskInstanceMapper.getBytaskTypeAndUsrUid(dhTaskInstance);
			if (taskExtis != null) {
				iterator.remove();//已经加签过给目标人员，则从列表移除
			}else {
				dhTaskInstance.setTaskUid("task_instance:" + UUID.randomUUID());
			}	
		}
		if(dhTaskInstances != null && dhTaskInstances.size()>0) {
			dhTaskInstanceMapper.insertBatch(dhTaskInstances);
			return ServerResponse.createBySuccessMessage("批量抄送成功");
		}else {
			return ServerResponse.createBySuccessMessage("批量抄送成功,无新增抄送通知");
		}
	}

	@Override
	@Transactional
	public ServerResponse allTransferTaskInstanceByUser(DhTurnTaskRecord dhTurnTaskRecord,
			DhTaskInstance dhTaskInstance, String isAgent,Date startTime,Date endTime) {
		dhTaskInstance.setTaskStatus("'32'");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectPageTaskByClosedByCondition(startTime, endTime, dhTaskInstance, isAgent);
		for(Iterator<DhTaskInstance> iterator = resultList.iterator(); iterator.hasNext();)
	     {
			DhTaskInstance taskInstance = iterator.next();
			taskInstance.setTaskType(DhTaskInstance.TYPE_TRANSFER);
			taskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
			taskInstance.setFromTaskUid(taskInstance.getTaskUid());
			taskInstance.setTaskInitDate(new Date());
			taskInstance.setTaskDueDate(null);
			taskInstance.setUsrUid(dhTurnTaskRecord.getTargetUserUid());
			DhTaskInstance taskExtis = dhTaskInstanceMapper.getBytaskTypeAndUsrUid(taskInstance);
			if (taskExtis != null) {
				iterator.remove();//已经加签过给目标人员，则从列表移除
			}else {
				taskInstance.setTaskUid("task_instance:" + UUID.randomUUID());
			}	
		}
		if(resultList != null && resultList.size()>0) {
			dhTaskInstanceMapper.insertBatch(resultList);
			return ServerResponse.createBySuccessMessage("批量抄送成功");
		}else {
			return ServerResponse.createBySuccessMessage("批量抄送成功,无新增抄送通知");
		}
	}

}
