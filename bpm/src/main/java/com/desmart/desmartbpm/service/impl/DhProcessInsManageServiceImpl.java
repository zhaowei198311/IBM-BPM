package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.DateUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.common.util.ProcessDataUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.LockedTask;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.mongo.TaskMongoDao;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhProcessInsManageService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 流程实例管理业务逻辑层实现类
 * 
 * @author loser_wu
 * @since 2018年5月22日
 */
@Service
@Transactional
public class DhProcessInsManageServiceImpl implements DhProcessInsManageService {
	private Logger log = Logger.getLogger(DhProcessInsManageServiceImpl.class);
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	@Autowired
	private DhRoutingRecordService dhRoutingRecordService;
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
	@Autowired
	private TaskMongoDao taskMongodao;

	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> getProcesssInstanceListByCondition(Integer pageNum,
			Integer pageSize, DhProcessInstance dhProcessInstance) {
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("ins.INS_UPDATE_DATE DESC");
		List<DhProcessInstance> resultList = dhProcessInstanceMapper
				.getProcesssInstanceListByCondition(dhProcessInstance);
		PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	@Transactional
	public ServerResponse terminateProcessIns(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		DhProcessInstance processInstance = new DhProcessInstance();
		processInstance.setInsId(dhProcessInstance.getInsId());
		List<DhProcessInstance> dhProcessInstances = dhProcessInstanceMapper.queryBySelective(processInstance);
		List<DhTaskInstance> taskInstances = dhTaskInstanceMapper.getDhTaskInstancesByBatch(dhProcessInstances);
		processInstance.setInsStatus(DhProcessInstance.STATUS_TERMINATED);
		processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_TERMINATED);
		Date updateDate = DateUtil.format(new Date());
		processInstance.setInsUpdateDate(updateDate);
		Integer count = dhProcessInstanceMapper.updateProcessInstanceByInsId(processInstance);
		if (count > 0) {
			DhTaskInstance dhTaskInstance = new DhTaskInstance();
			dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_DISCARD);
			dhTaskInstance.setInsUpdateDate(updateDate);
			dhTaskInstanceMapper.updateTaskStatusByBatch(taskInstances, dhTaskInstance);
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
			HttpReturnStatus httpReturn = bpmProcessUtil
					.terminateInstance(String.valueOf(dhProcessInstance.getInsId()));
			if (httpReturn.getCode() == -1) {
				throw new RuntimeException(httpReturn.getMsg());
			} else {
				return ServerResponse.createBySuccessMessage("终止流程实例成功！");
			}
		} else {
			return ServerResponse.createByErrorMessage("终止流程实例失败！");
		}
	}

	@Override
	@Transactional
	public ServerResponse pauseProcessIns(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		if (DhProcessInstance.STATUS_ID_ACTIVE == dhProcessInstance.getInsStatusId()) {
			DhProcessInstance processInstance = new DhProcessInstance();
			processInstance.setInsId(dhProcessInstance.getInsId());
			List<DhProcessInstance> dhProcessInstances = dhProcessInstanceMapper.queryBySelective(processInstance);
			List<DhTaskInstance> taskInstances = dhTaskInstanceMapper.getDhTaskInstancesByBatch(dhProcessInstances);
			processInstance.setInsStatus(DhProcessInstance.STATUS_SUSPENDED);
			processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_SUSPENDED);
			Date updateDate = DateUtil.format(new Date());
			processInstance.setInsUpdateDate(updateDate);
			Integer count = dhProcessInstanceMapper.updateProcessInstanceByInsId(processInstance);
			if (count > 0) {
				for (DhTaskInstance dhTaskInstance : taskInstances) {
					dhTaskInstance.setInsUpdateDate(updateDate);
					dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_PAUSE + dhTaskInstance.getTaskStatus());
				}
				dhTaskInstanceMapper.updateTaskByBatch(taskInstances);
				BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
				BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
				HttpReturnStatus httpReturn = bpmProcessUtil
						.pauseInstance(String.valueOf(dhProcessInstance.getInsId()));
				if (httpReturn.getCode() == -1) {
					throw new RuntimeException(httpReturn.getMsg());
				} else {
					return ServerResponse.createBySuccessMessage("暂停流程实例成功！");
				}
			} else {
				return ServerResponse.createByErrorMessage("暂停流程实例失败！");
			}
		} else {
			return ServerResponse.createByErrorMessage("请选择运转中的流程实例");
		}
	}

	@Override
	@Transactional
	public ServerResponse resumeProcessIns(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		if (DhProcessInstance.STATUS_ID_SUSPENDED == dhProcessInstance.getInsStatusId()) {
			DhProcessInstance processInstance = new DhProcessInstance();
			processInstance.setInsId(dhProcessInstance.getInsId());
			List<DhProcessInstance> dhProcessInstances = dhProcessInstanceMapper.queryBySelective(processInstance);
			List<DhTaskInstance> taskInstances = dhTaskInstanceMapper.getDhTaskInstancesByBatch(dhProcessInstances);
			processInstance.setInsStatus(DhProcessInstance.STATUS_ACTIVE);
			processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
			Date updateDate = DateUtil.format(new Date());
			processInstance.setInsUpdateDate(updateDate);
			Integer count = dhProcessInstanceMapper.updateProcessInstanceByInsId(processInstance);
			if (count > 0) {
				for (DhTaskInstance dhTaskInstance : taskInstances) {
					dhTaskInstance.setInsUpdateDate(updateDate);
					dhTaskInstance
							.setTaskStatus(dhTaskInstance.getTaskStatus().replaceAll(DhTaskInstance.STATUS_PAUSE, ""));
				}
				dhTaskInstanceMapper.updateTaskByBatch(taskInstances);
				BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
				BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
				HttpReturnStatus httpReturn = bpmProcessUtil
						.resumeInstance(String.valueOf(dhProcessInstance.getInsId()));
				if (httpReturn.getCode() == -1) {
					throw new RuntimeException(httpReturn.getMsg());
				} else {
					return ServerResponse.createBySuccessMessage("恢复流程实例成功！");
				}
			} else {
				return ServerResponse.createByErrorMessage("恢复流程实例失败！");
			}
		} else {
			return ServerResponse.createByErrorMessage("请选择暂挂的流程实例");
		}
	}

	@Override
	public ServerResponse getProcessInsInfo(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		HttpReturnStatus httpReturn = bpmProcessUtil.getProcessData(dhProcessInstance.getInsId());
		if (httpReturn.getCode() != -1) {
			return ServerResponse.createBySuccessMessage(httpReturn.getMsg());
		} else {
			return ServerResponse.createByErrorMessage(httpReturn.getMsg());
		}
	}

	@Override
	public ServerResponse toTrunOffProcessIns(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		if (DhProcessInstance.STATUS_ID_ACTIVE == dhProcessInstance.getInsStatusId()) {
			ServerResponse serverResponse = this.getCanTrunOffActivityMetas(dhProcessInstance);
			if (serverResponse.isSuccess()) {
				
				DhTaskInstance dhTaskInstance = new DhTaskInstance();
				dhTaskInstance.setInsUid(dhProcessInstance.getInsUid());
				List<DhTaskInstance> list = dhTaskInstanceMapper.selectBackLogTaskInfoByCondition(null, null,
						dhTaskInstance);
				Map<String, Object> data = new HashMap<>();
				data.put("activityMetaList", serverResponse.getData());
				data.put("taskList", list);
				return ServerResponse.createBySuccess(data);
			} else {
				return serverResponse;
			}
		} else {
			return ServerResponse.createByErrorMessage("请选择运转中的流程实例");
		}
	}

	@Override
	@Transactional
	public ServerResponse trunOffProcessIns(String taskUid, String activityId, String userUid, String trunOffCause) {
		// 判断选择的任务是否存在
		if (StringUtils.isBlank(taskUid)) {
			return ServerResponse.createByErrorMessage("缺少任务信息");
		}
		DhTaskInstance currTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (currTaskInstance == null || !"12".equals(currTaskInstance.getTaskStatus())) {
			return ServerResponse.createByErrorMessage("任务不存在，或状态异常");
		}
		String currActivityId = currTaskInstance.getTaskActivityId();
		BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(currActivityId);

		// 判断当前节点能否撤转, 如果不是普通节点不允许撤转
		if (!"none".equals(currTaskNode.getLoopType())) {
			return ServerResponse.createByErrorMessage("当前节点不允许撤转");
		}
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTaskInstance.getInsUid());
		if (dhProcessInstance == null)
			return ServerResponse.createByErrorMessage("流程实例不存在");

		int taskId = currTaskInstance.getTaskId();
		int insId = dhProcessInstance.getInsId();

		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		// 获取树流程信息
		HttpReturnStatus returnStatus = bpmProcessUtil.getProcessData(insId);
		if (HttpReturnStatusUtil.isErrorResult(returnStatus)) {
			return ServerResponse.createByErrorMessage("查询树流程信息出错");
		}

		// 获得任务的token
		JSONObject jsonObject = JSONObject.parseObject(returnStatus.getMsg());
		String tokenId = ProcessDataUtil.getTokenIdOfTask(taskId, jsonObject);
		if (StringUtils.isBlank(tokenId)) {
			return ServerResponse.createByErrorMessage("撤转失败，找不到tokenID");
		}
		// 目标环节
		BpmActivityMeta targetActivityMeta = bpmActivityMetaService.queryByPrimaryKey(activityId);
		Date updateDate = DateUtil.format(new Date());
		if (targetActivityMeta != null) {// 目标环节不为空，即撤转任务
			//判断目标节点能否撤转
			if (!"none".equals(targetActivityMeta.getLoopType())) {
				return ServerResponse.createByErrorMessage("目标节点不允许撤转");
			}
			// 保存流转记录
			DhRoutingRecord routingRecord = dhRoutingRecordService
					.generateTrunOffTaskRoutingRecordByTaskAndRoutingData(currTaskInstance, targetActivityMeta);
			dhRoutingRecordMapper.insert(routingRecord);

			String targetActivityBpdId = targetActivityMeta.getActivityBpdId();

			// 关闭当前的任务
			List<DhTaskInstance> taskInstances = new ArrayList<>();
			taskInstances.add(currTaskInstance);
			DhTaskInstance dhTaskInstance = new DhTaskInstance();
			dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_DISCARD);
			dhTaskInstance.setInsUpdateDate(updateDate);
			dhTaskInstanceMapper.updateTaskStatusByBatch(taskInstances, dhTaskInstance);

			// 关闭关联的任务（同一个节点上的任务）
			dhTaskInstanceService.abandonRelationTaskOnTaskNode(currTaskInstance);

			HttpReturnStatus httpReturnStatus = bpmProcessUtil.moveToken(insId, targetActivityBpdId, tokenId);
			if (HttpReturnStatusUtil.isErrorResult(httpReturnStatus)) {
				throw new PlatformException("调用API 撤转失败");
			}
			// 移动token成功
			JSONObject processData = JSON.parseObject(httpReturnStatus.getMsg());
			List<Integer> taskIdList = ProcessDataUtil.getActiveTaskIdByFlowObjectId(targetActivityBpdId, processData);
			// 阻止拉取任务
			taskMongodao.batchlockTasks(taskIdList, LockedTask.REASON_TRUN_OFF_TASK);
			
			String[] taskOwners = userUid.split(";");
			for (String taskOwner : taskOwners) {
				BpmTaskUtil taskUtil = new BpmTaskUtil(bpmGlobalConfig);
				ServerResponse serverResponse = taskUtil.changeOwnerOfLaswTask(taskIdList.get(0), taskOwner);
				if (!serverResponse.isSuccess()) {
					log.error("重新分配失败");
					return serverResponse;
				}	
			}

			dhTaskInstanceService.saveTaskToRetryTable(taskIdList.get(0));
			taskMongodao.removeLockedTask(taskIdList.get(0));
	        
		} else {// 目标环节为空，即为选中的任务更换任务处理人--只能选中一个任务处理人
			String[] userUids = userUid.split(";");
			currTaskInstance.setUsrUid(userUids[0]);
			currTaskInstance.setInsUpdateDate(updateDate);
			dhTaskInstanceMapper.updateByPrimaryKeySelective(currTaskInstance);
			
			// 保存流转记录
			DhRoutingRecord routingRecord = dhRoutingRecordService
						.generateTrunOffTaskRoutingRecordByTaskAndRoutingData(currTaskInstance, currTaskNode);
			dhRoutingRecordMapper.insert(routingRecord);
			BpmTaskUtil taskUtil = new BpmTaskUtil(bpmGlobalConfig);
			ServerResponse serverResponse = 
						taskUtil.changeOwnerOfLaswTask(currTaskInstance.getTaskId(), userUids[0]);
			if (!serverResponse.isSuccess()) {
				log.error("重新分配失败");
				return serverResponse;
			}	
		}
		return ServerResponse.createBySuccessMessage("撤转流程实例成功");
	}

	@Override
	public ServerResponse getCanTrunOffActivityMetas(DhProcessInstance dhProcessInstance) {
		BpmActivityMeta selective = new BpmActivityMeta(dhProcessInstance.getProAppId()
					, dhProcessInstance.getProUid(), dhProcessInstance.getProVerUid());
        selective.setBpmTaskType(BpmActivityMeta.BPM_TASK_TYPE_USER_TASK);
        List<BpmActivityMeta> humanActivities = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);

		List<BpmActivityMeta> bpmActivityMetas = new ArrayList<>();
		if(dhProcessInstance.getTokenActivityId()!=null) {//子流程
			//找到当前流程实例所停留的主流程的环节
			BpmActivityMeta currActivityMeta = bpmActivityMetaService
					.queryByPrimaryKey(dhProcessInstance.getTokenActivityId());
			BpmActivityMeta selective1 = new BpmActivityMeta();
			selective1.setActivityId(currActivityMeta.getActivityId());
			selective1.setBpmTaskType(BpmActivityMeta.BPM_TASK_TYPE_USER_TASK);
			//根据所停留的主流程环节找出子级的环节节点
			bpmActivityMetas = bpmActivityMetaMapper
					.queryChildrenMetaByCondition(selective1);
		}else {//主流程
			//过滤出主流程的人工环节节点
			List<BpmActivityMeta> mainMetas = bpmActivityMetaService
							.filterBasicActivity(humanActivities);
			//过滤内连子流程环节--parentId不等于0的
			for (BpmActivityMeta bpmActivityMeta : mainMetas) {
				if(StringUtils.equals(bpmActivityMeta.getParentActivityId(), "0")) {
					bpmActivityMetas.add(bpmActivityMeta);
				}
			}
		}
		return ServerResponse.createBySuccess(bpmActivityMetas);
	}
}
