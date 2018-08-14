package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysUser;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DataForSkipFromReject;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.github.pagehelper.PageHelper;
import org.springframework.util.CollectionUtils;

@Service
public class DhRoutingRecordServiceImpl implements DhRoutingRecordService {

    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public List<DhRoutingRecord> getDhRoutingRecordListByCondition(DhRoutingRecord dhRoutingRecord) {
		List<DhRoutingRecord> dhRoutingRecordList = dhRoutingRecordMapper.getDhRoutingRecordListByCondition(dhRoutingRecord);
		for(DhRoutingRecord oldRoutingRecord:dhRoutingRecordList) {
			//判读流转信息中的处理人id是否和代理人id相同
			if(oldRoutingRecord.getUserUid().equals(oldRoutingRecord.getAgentUserUid())) {
				//当流转信息中处理人id和代理人id相同时，查询对应的任务实例中的处理人信息
				SysUser handler = sysUserMapper.queryByPrimaryKey(oldRoutingRecord.getTaskHandleUserId());
				oldRoutingRecord.setTaskHandleUserName(handler.getUserName());
				oldRoutingRecord.setStation(handler.getStation());
			}else {
				continue;
			}
		}
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
    public DhRoutingRecord generateRejectTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance, BpmActivityMeta targetNode) {
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
    public DhRoutingRecord generateAutoCommitRoutingRecord(DhTaskInstance currTaskInstance, BpmActivityMeta tagetActivityMeta
        , String adminUid) {
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

}
