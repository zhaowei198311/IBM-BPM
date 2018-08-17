package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DataForSkipFromReject;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.DatDhRoutingRecord;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.github.pagehelper.PageHelper;

@Service
public class DhRoutingRecordServiceImpl implements DhRoutingRecordService {

	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public List<DhRoutingRecord> getDhRoutingRecordListByCondition(DhRoutingRecord dhRoutingRecord) {
		List<DhRoutingRecord> dhRoutingRecordList = dhRoutingRecordMapper
				.getDhRoutingRecordListByCondition(dhRoutingRecord);
		return dhRoutingRecordList;
	}

	@Override
	public int saveDhRoutingRecord(DhRoutingRecord dhRoutingRecord) {
		return dhRoutingRecordMapper.insert(dhRoutingRecord);
	}

	@Override
	public DhRoutingRecord generateSubmitTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance,
			BpmRoutingData bpmRoutingData, boolean willTokenMove) {
		BpmActivityMeta taskNode = bpmActivityMetaService.queryByPrimaryKey(taskInstance.getTaskActivityId());

		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(taskInstance.getInsUid());
		dhRoutingRecord.setActivityName(taskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_SUBMIT_TASK);
		// 路由记录发生人
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(taskInstance.getTaskActivityId());
		dhRoutingRecord.setTaskUid(taskInstance.getTaskUid());
		String activityTo = null;
		StringBuilder activityToBuilder = new StringBuilder();

		if (willTokenMove) {
			// 如果token要移动的话，处理接下来的人员环节
			List<BpmActivityMeta> normalNodes = bpmRoutingData.getNormalNodes();
			for (BpmActivityMeta nextNode : normalNodes) {
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
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateFirstTaskNodeOfSubProcessRoutingData(DhTaskInstance taskInstance,
			BpmRoutingData bpmRoutingData, String userUid) {
		BpmActivityMeta taskNode = bpmActivityMetaService.queryByPrimaryKey(taskInstance.getTaskActivityId());

		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(taskInstance.getInsUid());
		dhRoutingRecord.setActivityName(taskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_SUBMIT_TASK);
		// 路由记录发生人
		dhRoutingRecord.setUserUid(userUid);
		dhRoutingRecord.setActivityId(taskInstance.getTaskActivityId());
		dhRoutingRecord.setTaskUid(taskInstance.getTaskUid());
		String activityTo = null;
		StringBuilder activityToBuilder = new StringBuilder();

		// token要移动的话，处理接下来的人员环节
		List<BpmActivityMeta> normalNodes = bpmRoutingData.getNormalNodes();
		for (BpmActivityMeta nextNode : normalNodes) {
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
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateSystemTaskRoutingRecord(BpmActivityMeta taskNode, DhTaskInstance currTask,
			String systemUser, BpmRoutingData bpmRoutingData) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(currTask.getInsUid());
		dhRoutingRecord.setActivityName(currTask.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_FINISH_SYSTEM_TASK);
		dhRoutingRecord.setUserUid(systemUser);
		dhRoutingRecord.setActivityId(currTask.getTaskActivityId());
		dhRoutingRecord.setTaskUid(currTask.getTaskUid());
		String activityTo = null;
		StringBuilder activityToBuilder = new StringBuilder();
		// 如果token要移动的话，处理接下来的人员环节
		List<BpmActivityMeta> normalNodes = bpmRoutingData.getNormalNodes();
		for (BpmActivityMeta nextNode : normalNodes) {
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
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateRejectTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance,
			BpmActivityMeta targetNode) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(taskInstance.getInsUid());
		dhRoutingRecord.setActivityName(taskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_REJECT_TASK);
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(taskInstance.getTaskActivityId());
		String activityTo = targetNode.getActivityId();
		dhRoutingRecord.setActivityTo(activityTo);
		dhRoutingRecord.setTaskUid(taskInstance.getTaskUid());
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateRevokeTaskRoutingRecord(DhTaskInstance finishedTaskInstance) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(finishedTaskInstance.getInsUid());
		dhRoutingRecord.setActivityName(finishedTaskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_REVOKE_TASK);
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(finishedTaskInstance.getTaskActivityId());
		String activityTo = finishedTaskInstance.getTaskActivityId();
		dhRoutingRecord.setActivityTo(activityTo);
		dhRoutingRecord.setTaskUid(finishedTaskInstance.getTaskUid());
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateAddTaskRoutingRecord(DhTaskInstance currTaskInstance) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(currTaskInstance.getInsUid());
		dhRoutingRecord.setActivityName(currTaskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_ADD_TASK);
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(currTaskInstance.getTaskActivityId());
		dhRoutingRecord.setTaskUid(currTaskInstance.getTaskUid());
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateFinishAddTaskRoutingRecord(DhTaskInstance currTaskInstance) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(currTaskInstance.getInsUid());
		dhRoutingRecord.setActivityName(currTaskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_FINISH_ADDTASK);
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(currTaskInstance.getTaskActivityId());
		dhRoutingRecord.setTaskUid(currTaskInstance.getTaskUid());
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateSkipFromRejectRoutingRecord(DataForSkipFromReject dataForSkipFromReject) {
		DhTaskInstance currTaskInstance = dataForSkipFromReject.getCurrTask();
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(currTaskInstance.getInsUid());
		dhRoutingRecord.setActivityName(currTaskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_SUBMIT_TASK);
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(currTaskInstance.getTaskActivityId());
		dhRoutingRecord.setActivityTo(dataForSkipFromReject.getTargetNode().getActivityId());
		dhRoutingRecord.setTaskUid(currTaskInstance.getTaskUid());
		return dhRoutingRecord;
	}

	@Override
	public ServerResponse loadDhRoutingRecords(String insUid) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setInsUid(insUid);
		// 获得当前流程实例的所有流转信息记录
		List<DhRoutingRecord> dhRoutingRecords = this.getDhRoutingRecordListByCondition(dhRoutingRecord);

		List<BpmActivityMeta> bpmActivityMetaList = new ArrayList<BpmActivityMeta>();// 保存当前流转到的所有环节
		List<DhTaskInstance> currentDhTaskHandlers = new ArrayList<DhTaskInstance>();// 保存当前要处理的任务相关的信息
		Map<String, DatDhRoutingRecord> currentTaskMap = new HashMap<>();//保存组装界面展示的数据
		// 从流转记录中找到最后条流转信息记录
		DhRoutingRecord lastDhRoutingRecord = getLastRoutingRecordWithActivityTo(dhRoutingRecords);
		if (lastDhRoutingRecord != null) {
			String activityToStr = lastDhRoutingRecord.getActivityTo();
			String[] activityTo = activityToStr.split(",");
			List<String> activityIdList = Arrays.asList(activityTo);
			if (activityIdList != null && activityIdList.size() > 0) {
				//根据最后一条流转信息，取得当前活动的流程环节集合
				bpmActivityMetaList = bpmActivityMetaMapper.queryPrimaryKeyByBatch(activityIdList);

				DhTaskInstance dhTaskInstanceSelect = new DhTaskInstance();
				dhTaskInstanceSelect.setInsUid(insUid);

				// 获得当前流程实例的所有任务（包含了处理人、代理人的uid和姓名岗位）
				List<DhTaskInstance> dhTaskInstances = dhTaskInstanceMapper.selectByCondition(dhTaskInstanceSelect);

				// 过滤出当前的任务
				for (DhTaskInstance taskInstance : dhTaskInstances) {
					String taskActivityId = taskInstance.getTaskActivityId();
					String taskStatus = taskInstance.getTaskStatus();
					String taskType = taskInstance.getTaskType();
					String matchedTaskType = DhTaskInstance.TYPE_NORMAL + ";" + DhTaskInstance.TYPE_SIMPLE_LOOP + ";"
							+ DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP;
					if (activityToStr.contains(taskActivityId) && matchedTaskType.contains(taskType)
							&& (taskStatus.equals(DhTaskInstance.STATUS_RECEIVED))) {
						currentDhTaskHandlers.add(taskInstance);
					}
				}
				
				//组装流转记录数据开始
				for (DhRoutingRecord oldRoutingRecord : dhRoutingRecords) {
					// 判读流转信息中的操作人id是否和代理人id相同.
					if (oldRoutingRecord.getUserUid().equals(oldRoutingRecord.getAgentUserUid())) {
						//从当前流程所有任务中过滤出当前流转记录对应的任务
						DhTaskInstance dhTaskInstance = 
								queryTaskListByPirmaryKey(oldRoutingRecord.getTaskUid(),dhTaskInstances);
						// 当流转信息中操作人id和代理人id相同时，查询对应的任务实例中的处理人信息替换操作人信息，便于前台展示
						if(dhTaskInstance!=null) {
						oldRoutingRecord.setTaskHandleUserName(dhTaskInstance.getTaskHandler());
						oldRoutingRecord.setStation(dhTaskInstance.getHandlerStation());
						}else {
							return ServerResponse.createByErrorMessage("流转记录任务信息出现异常");
						}
					} else {
						continue;
					}
				}
				//按照TASK_ACTIVITY_ID分组当前任务的集合和环节
				 currentTaskMap = 
						groupListToMapByTaskActivityId(currentDhTaskHandlers,bpmActivityMetaList);
			}
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bpmActivityMetaList", bpmActivityMetaList);
		data.put("dhRoutingRecords", dhRoutingRecords);
		data.put("dhTaskHandlers", currentDhTaskHandlers);
		data.put("currentTaskMap", currentTaskMap);
		return ServerResponse.createBySuccess(data);
	}
	/**
	 * 从任务集合中过滤出taskUid对应的任务
	 * @param taskUid
	 * @param dhTaskInstances
	 * @return
	 */
	private DhTaskInstance queryTaskListByPirmaryKey(String taskUid, List<DhTaskInstance> dhTaskInstances) {
		for (DhTaskInstance dhTaskInstance : dhTaskInstances) {
			if(dhTaskInstance.getTaskUid().equals(taskUid)) {
				return dhTaskInstance;
			}
		}
		return null;
	}

	/**
	 * 从流转记录列表中找到最近的流转记录
	 * 
	 * @param routingRecords
	 *            此流程下所有的流转记录
	 * @return
	 */
	private DhRoutingRecord getLastRoutingRecordWithActivityTo(List<DhRoutingRecord> routingRecords) {
		if (routingRecords == null || routingRecords.isEmpty()) {
			return null;
		}
		for (int i = routingRecords.size() - 1; i >= 0; i--) {
			DhRoutingRecord dhRoutingRecord = routingRecords.get(i);
			if (StringUtils.isNotBlank(dhRoutingRecord.getActivityTo())) {
				return dhRoutingRecord;
			}
		}
		return null;
	}

	@Override
	public List<DhRoutingRecord> getAllRoutingRecordOfProcessInstance(String insUid) {
		DhRoutingRecord recordSelective = new DhRoutingRecord();
		recordSelective.setInsUid(insUid);
		PageHelper.orderBy("CREATE_TIME DESC");
		return dhRoutingRecordMapper.listBySelective(recordSelective);
	}

	@Override
	public DhRoutingRecord getSubmitRoutingRecordOfTask(String taskUid) {
		DhRoutingRecord recordSelective = new DhRoutingRecord();
		recordSelective.setTaskUid(taskUid);
		List<DhRoutingRecord> dhRoutingRecords = dhRoutingRecordMapper.listBySelective(recordSelective);
		if (dhRoutingRecords.isEmpty()) {
			return null;
		}
		for (DhRoutingRecord dhRoutingRecord : dhRoutingRecords) {
			if (dhRoutingRecord.getRouteType().equals(DhRoutingRecord.ROUTE_TYPE_SUBMIT_TASK)) {
				return dhRoutingRecord;
			}
		}
		return null;
	}

	@Override
	public List<DhRoutingRecord> getRoutingRecordOfTask(String taskUid) {
		DhRoutingRecord recordSelective = new DhRoutingRecord();
		recordSelective.setTaskUid(taskUid);
		return dhRoutingRecordMapper.listBySelective(recordSelective);
	}

	@Override
	public DhRoutingRecord generateTrunOffTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance currTaskInstance,
			BpmActivityMeta tagetActivityMeta) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(currTaskInstance.getInsUid());
		dhRoutingRecord.setActivityName(currTaskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_TRUN_OFF_TASK);
		dhRoutingRecord.setUserUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhRoutingRecord.setActivityId(currTaskInstance.getTaskActivityId());
		String activityTo = tagetActivityMeta.getActivityId();
		dhRoutingRecord.setActivityTo(activityTo);
		dhRoutingRecord.setTaskUid(currTaskInstance.getTaskUid());
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord generateAutoCommitRoutingRecord(DhTaskInstance currTaskInstance,
			BpmActivityMeta tagetActivityMeta, String adminUid) {
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(currTaskInstance.getInsUid());
		dhRoutingRecord.setActivityName(currTaskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_TYPE_AUTOCOMMIT);
		dhRoutingRecord.setUserUid(adminUid); // 路由人是管理员
		dhRoutingRecord.setActivityId(currTaskInstance.getTaskActivityId());
		String activityTo = tagetActivityMeta.getActivityId();
		dhRoutingRecord.setActivityTo(activityTo);
		dhRoutingRecord.setTaskUid(currTaskInstance.getTaskUid());
		return dhRoutingRecord;
	}

	@Override
	public DhRoutingRecord getLatestRoutingRecordByActivityToAndInsUid(String activityTo, String insUid) {
		DhRoutingRecord recordSelective = new DhRoutingRecord();
		recordSelective.setInsUid(insUid);
		recordSelective.setActivityTo(activityTo);
		PageHelper.orderBy("create_time desc");
		List<DhRoutingRecord> dhRoutingRecords = dhRoutingRecordMapper.listBySelective(recordSelective);
		if (CollectionUtils.isEmpty(dhRoutingRecords)) {
			return null;
		} else {
			return dhRoutingRecords.get(0);
		}
	}
	/**
	 * 根据TASK_ACTIVITY_ID分组任务和环节
	 * @param list
	 * @param bpmActivityMetaList
	 * @return
	 */
	private Map<String, DatDhRoutingRecord> groupListToMapByTaskActivityId(List<DhTaskInstance> list
				,List<BpmActivityMeta> bpmActivityMetaList) {

		Map<String, DatDhRoutingRecord> map = new HashMap<>();
		
		for (DhTaskInstance dhTaskInstance : list) {
			DatDhRoutingRecord datDhRoutingRecord = new DatDhRoutingRecord();
			if(dhTaskInstance!=null) {
			String key = dhTaskInstance.getTaskActivityId();
			if(map.keySet()!=null&&map.keySet().size()>0) {
				if(map.keySet().contains(key)) {//判断该taskActivityId是否已经分组过，分组过则跳过此次循环
					continue;
				}
			}
			List<DhTaskInstance> sonList = new ArrayList<>();
			for (DhTaskInstance son : list) {
				if(son!=null) {
				if (key != null) {
					if (key.equals(son.getTaskActivityId())) {//将相同taskActivityId的任务添加到一个集合
						sonList.add(son);
					}
				}
				}
			}
			for (BpmActivityMeta bpmActivityMeta : bpmActivityMetaList) {
				if(key.equals(bpmActivityMeta.getActivityId())) {//找出对应的环节
					datDhRoutingRecord.setBpmActivityMeta(bpmActivityMeta);
				}
			}
			datDhRoutingRecord.setDhTaskInstanceList(sonList);
			map.put(key, datDhRoutingRecord);
		  }
		}
		return map;
	}

}
