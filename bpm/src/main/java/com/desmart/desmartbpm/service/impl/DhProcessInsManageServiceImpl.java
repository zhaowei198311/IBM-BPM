package com.desmart.desmartbpm.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.DateUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.service.DhProcessInsManageService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 流程实例管理业务逻辑层实现类
 * @author loser_wu
 * @since 2018年5月22日
 */
@Service
@Transactional
public class DhProcessInsManageServiceImpl implements DhProcessInsManageService{
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;

	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> getProcesssInstanceListByCondition(Integer pageNum,Integer pageSize,
					DhProcessInstance dhProcessInstance) {
		PageHelper.startPage(pageNum,pageSize);
		List<DhProcessInstance> resultList = dhProcessInstanceMapper.getProcesssInstanceListByCondition(dhProcessInstance);
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
		if(count>0) {
			DhTaskInstance dhTaskInstance = new DhTaskInstance();
			dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_DISCARD);
			dhTaskInstance.setInsUpdateDate(updateDate);
			dhTaskInstanceMapper.updateTaskStatusByBatch(taskInstances,dhTaskInstance);
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
			HttpReturnStatus httpReturn = bpmProcessUtil.terminateInstance(String.valueOf(dhProcessInstance.getInsId()));
			if(httpReturn.getCode()==-1) {
					throw new RuntimeException(httpReturn.getMsg());
			}else {
				return ServerResponse.createBySuccessMessage("终止流程实例成功！");
			}
		}else {
			return ServerResponse.createByErrorMessage("终止流程实例失败！");
		}
	}

	@Override
	@Transactional
	public ServerResponse pauseProcessIns(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		if(DhProcessInstance.STATUS_ID_ACTIVE==dhProcessInstance.getInsStatusId()) {
		DhProcessInstance processInstance = new DhProcessInstance();
		processInstance.setInsId(dhProcessInstance.getInsId());
		List<DhProcessInstance> dhProcessInstances = dhProcessInstanceMapper.queryBySelective(processInstance);
		List<DhTaskInstance> taskInstances = dhTaskInstanceMapper.getDhTaskInstancesByBatch(dhProcessInstances);
		processInstance.setInsStatus(DhProcessInstance.STATUS_SUSPENDED);
		processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_SUSPENDED);
		Date updateDate = DateUtil.format(new Date());
		processInstance.setInsUpdateDate(updateDate);
		Integer count = dhProcessInstanceMapper.updateProcessInstanceByInsId(processInstance);
		if(count>0) {
			for (DhTaskInstance dhTaskInstance : taskInstances) {
				dhTaskInstance.setInsUpdateDate(updateDate);
				dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_PAUSE+dhTaskInstance.getTaskStatus());
			}
			dhTaskInstanceMapper.updateTaskByBatch(taskInstances);
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
			HttpReturnStatus httpReturn = bpmProcessUtil.pauseInstance(String.valueOf(dhProcessInstance.getInsId()));
			if(httpReturn.getCode()==-1) {
				throw new RuntimeException(httpReturn.getMsg());
			}else {
				return ServerResponse.createBySuccessMessage("暂停流程实例成功！");
			}
		}else {
			return ServerResponse.createByErrorMessage("暂停流程实例失败！");
		}
		}else {
			return ServerResponse.createByErrorMessage("请选择运转中的流程实例");
		}
	}

	@Override
	@Transactional
	public ServerResponse resumeProcessIns(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		if(DhProcessInstance.STATUS_ID_SUSPENDED == dhProcessInstance.getInsStatusId()) {
		DhProcessInstance processInstance = new DhProcessInstance();
		processInstance.setInsId(dhProcessInstance.getInsId());
		List<DhProcessInstance> dhProcessInstances = dhProcessInstanceMapper.queryBySelective(processInstance);
		List<DhTaskInstance> taskInstances = dhTaskInstanceMapper.getDhTaskInstancesByBatch(dhProcessInstances);
		processInstance.setInsStatus(DhProcessInstance.STATUS_ACTIVE);
		processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
		Date updateDate = DateUtil.format(new Date());
		processInstance.setInsUpdateDate(updateDate);
		Integer count = dhProcessInstanceMapper.updateProcessInstanceByInsId(processInstance);
		if(count>0) {
			for (DhTaskInstance dhTaskInstance : taskInstances) {
				dhTaskInstance.setInsUpdateDate(updateDate);
				dhTaskInstance.setTaskStatus(dhTaskInstance.getTaskStatus()
						.replaceAll(DhTaskInstance.STATUS_PAUSE, ""));
			}
			dhTaskInstanceMapper.updateTaskByBatch(taskInstances);
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
			HttpReturnStatus httpReturn = bpmProcessUtil.resumeInstance(String.valueOf(dhProcessInstance.getInsId()));
			if(httpReturn.getCode()==-1) {
				throw new RuntimeException(httpReturn.getMsg());
			}else {
				return ServerResponse.createBySuccessMessage("恢复流程实例成功！");
			}
		}else {
			return ServerResponse.createByErrorMessage("恢复流程实例失败！");
		}
		}else {
			return ServerResponse.createByErrorMessage("请选择暂挂的流程实例");
		}
	}

	@Override
	public ServerResponse getProcessInsInfo(DhProcessInstance dhProcessInstance) {
		dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		HttpReturnStatus httpReturn = bpmProcessUtil.getProcessData(dhProcessInstance.getInsId());
		if(httpReturn.getCode()!=-1) {
			return ServerResponse.createBySuccessMessage(httpReturn.getMsg());
		}else {
			return ServerResponse.createByErrorMessage(httpReturn.getMsg());
		}
	}

	@Override
	public ServerResponse trunOffProcessIns(DhProcessInstance dhProcessInstance) {
		
		return null;
	}
}
