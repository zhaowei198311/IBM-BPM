package com.desmart.desmartbpm.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.DhTaskCheckException;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.CommonBusinessObjectUtils;
import com.desmart.common.util.DataListUtils;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.service.AutoCommitService;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhApprovalOpinionService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

@Service
public class AutoCommitServiceImpl implements AutoCommitService {
    private static final Logger LOG = LoggerFactory.getLogger(AutoCommitServiceImpl.class);

    @Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;
    @Autowired
    private DhTaskInstanceService dhTaskInstanceService;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
    @Autowired
    private DhRouteService dhRouteService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhRoutingRecordService dhRoutingRecordService;
    @Autowired
    private DhApprovalOpinionService dhApprovalOpinionService;

    @Scheduled(cron = "0/20 * * * * ?")
    public void startAutoCommit() {
        LOG.info("开始自动提交");
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        List<DhTaskInstance> taskList = getTaskListToAutoCommit(bpmGlobalConfig);
        for (DhTaskInstance taskInstance : taskList) {
            try {
                doAutoCommitTask(taskInstance, bpmGlobalConfig);
            } catch (Exception e) {
                LOG.error("自动提交失败：taskUid：" + taskInstance.getTaskUid(), e);
            }
        }
        LOG.info("自动提交完成");
    }

    /**
     * 自动提交指定任务
     * @param currTask
     * @param bpmGlobalConfig
     */
    public void doAutoCommitTask(DhTaskInstance currTask, BpmGlobalConfig bpmGlobalConfig) {
        BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(currTask.getTaskActivityId());
        if (currTaskNode == null) {
            throw new DhTaskCheckException("自动提交失败，找不到任务节点，taskUid：" + currTask.getTaskUid());
        }
        DhActivityConf currTaskConf = currTaskNode.getDhActivityConf();
        if (!"TRUE".equals(currTaskConf.getActcCanAutocommit())) {
            return;
        }
        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(currTaskNode.getBpmTaskType())
                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(currTaskNode.getLoopType())) {
            return;
        }
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());
        if (dhProcessInstance == null) {
            throw new DhTaskCheckException("自动提交失败，流程实例不存在");
        }
        // 获得下个任务节点
        // 下个环节只有一个，
        String activityTo = currTaskNode.getActivityTo();
        if (StringUtils.isBlank(activityTo) || activityTo.contains(",")) {
            throw new DhTaskCheckException("下个环节不存在或存在多个下个环节");
        }
        // 获得下个节点信息, 下个环节是普通的人员节点
        BpmActivityMeta nextTaskNode = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityTo, currTaskNode.getParentActivityId(),
                currTaskNode.getSnapshotId());
        if (nextTaskNode == null) {
            return;
        }
        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(nextTaskNode.getBpmTaskType())
                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(nextTaskNode.getLoopType())) {
            return;
        }
        // 获得下个任务的默认处理人信息，对下个任务来说，上个环节的处理人就是 当前任务的原处理人，非管理员，此时需要找到上个环节
        ServerResponse<BpmActivityMeta> preActivityReturn = dhRouteService.getPreActivityByDiagram(dhProcessInstance, currTaskNode);
        if (!preActivityReturn.isSuccess()) {
            throw new PlatformException("自动提交失败，获得环节的真正处理人失败");
        }
        BpmActivityMeta preMeta = preActivityReturn.getData();
        String originalUser = preMeta.getUserUid(); // 任务实际所有者
        JSONObject insDataJson = JSON.parseObject(dhProcessInstance.getInsData());
        List<SysUser> defaultTaskOwnerList = dhRouteService.getDefaultTaskOwnerOfTaskNode(nextTaskNode, originalUser,
                dhProcessInstance, insDataJson.getJSONObject("formData"));
        if (defaultTaskOwnerList.isEmpty()) {
            throw new PlatformException("自动提交失败，缺少默认处理人");
        }
        // 保存流转记录
        DhRoutingRecord dhRoutingRecord = dhRoutingRecordService.generateAutoCommitRoutingRecord(currTask,
                nextTaskNode, bpmGlobalConfig.getBpmAdminName());
        dhRoutingRecordService.saveDhRoutingRecord(dhRoutingRecord);
        // 如果需要填写审批意见，保存审批意见
        if ("TRUE".equals(currTaskConf.getActcCanApprove())) {
            dhApprovalOpinionService.saveDhApprovalOpinionWhenAutoCommitUserTask(currTask, bpmGlobalConfig.getBpmAdminName());
        }
        // 改变当前任务状态
        dhTaskInstanceService.updateDhTaskInstanceWhenAutoCommit(currTask, originalUser);

        String assignVariable = currTaskConf.getActcAssignVariable();
        CommonBusinessObject pubBo = new CommonBusinessObject(dhProcessInstance.getInsId());
        CommonBusinessObjectUtils.setNextOwners(assignVariable, pubBo, DataListUtils.transformUserListToUserIdList(defaultTaskOwnerList));

        BpmTaskUtil taskUtil = new BpmTaskUtil(bpmGlobalConfig);
        Map<String, HttpReturnStatus> commitTaskReturn = taskUtil.commitTask(currTask.getTaskId(), pubBo, null);
        Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(commitTaskReturn);
        if (errorMap.get("errorResult") != null) {
            throw new PlatformException("自动提交失败, 调用RESTful API 完成任务失败");
        }
        // RESTful API 调用成功，结束
    }


//    private DataForSubmitTask prepareDataForSubmitAutoCommitTask(DhTaskInstance currTask, BpmGlobalConfig bpmGlobalConfig) {
//        DataForSubmitTask dataForSubmitTask = null;
//
//        BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(currTask.getTaskActivityId());
//        if (currTaskNode == null) {
//            throw new DhTaskCheckException("自动提交失败，找不到任务节点，taskUid：" + currTask.getTaskUid());
//        }
//        DhActivityConf currTaskConf = currTaskNode.getDhActivityConf();
//
//        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(currTaskNode.getBpmTaskType())
//                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(currTaskNode.getLoopType())) {
//            throw new DhTaskCheckException("自动提交失败，任务类型不符合要求（简单循环或多实例）");
//        }
//        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());
//        if (dhProcessInstance == null) {
//            throw new DhTaskCheckException("自动提交失败，流程实例不存在");
//        }
//        // 获得下个任务节点
//        // 下个环节只有一个，
//        String activityTo = currTaskNode.getActivityTo();
//        if (StringUtils.isBlank(activityTo) || activityTo.contains(",")) {
//            throw new DhTaskCheckException("下个环节不存在或存在多个下个环节");
//        }
//        // 获得下个节点信息, 下个环节是普通的人员节点
//        BpmActivityMeta nextTaskNode = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityTo, currTaskNode.getParentActivityId(),
//                currTaskNode.getSnapshotId());
//        if (nextTaskNode == null) {
//            throw new DhTaskCheckException("获得下个节点失败");
//        }
//        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(nextTaskNode.getBpmTaskType())
//                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(nextTaskNode.getLoopType())) {
//            return;
//        }
//        // 获得下个任务的默认处理人信息，对下个任务来说，上个环节的处理人就是 当前任务的原处理人，非管理员，此时需要找到上个环节
//        ServerResponse<BpmActivityMeta> preActivityReturn = dhRouteService.getPreActivityByDiagram(dhProcessInstance, currTaskNode);
//        if (!preActivityReturn.isSuccess()) {
//            throw new PlatformException("自动提交失败，获得环节的真正处理人失败");
//        }
//        BpmActivityMeta preMeta = preActivityReturn.getData();
//        String originalUser = preMeta.getUserUid(); // 任务实际所有者
//        JSONObject insDataJson = JSON.parseObject(dhProcessInstance.getInsData());
//        List<SysUser> defaultTaskOwnerList = dhRouteService.getDefaultTaskOwnerOfTaskNode(nextTaskNode, originalUser,
//                dhProcessInstance, insDataJson.getJSONObject("formData"));
//        if (defaultTaskOwnerList.isEmpty()) {
//            throw new PlatformException("自动提交失败，缺少默认处理人");
//        }
//        // 保存流转记录
//        DhRoutingRecord dhRoutingRecord = dhRoutingRecordService.generateAutoCommitRoutingRecord(currTask,
//                nextTaskNode, bpmGlobalConfig.getBpmAdminName());
//        dhRoutingRecordService.saveDhRoutingRecord(dhRoutingRecord);
//        // 如果需要填写审批意见，保存审批意见
//        if ("TRUE".equals(currTaskConf.getActcCanApprove())) {
//            dhApprovalOpinionService.saveDhApprovalOpinionWhenAutoCommitUserTask(currTask, bpmGlobalConfig.getBpmAdminName());
//        }
//        // 改变当前任务状态
//        dhTaskInstanceService.updateDhTaskInstanceWhenAutoCommit(currTask, originalUser);
//
//        String assignVariable = currTaskConf.getActcAssignVariable();
//        CommonBusinessObject pubBo = new CommonBusinessObject(dhProcessInstance.getInsId());
//        CommonBusinessObjectUtils.setNextOwners(assignVariable, pubBo, DataListUtils.transformUserListToUserIdList(defaultTaskOwnerList));
//
//
//        List<DhStep> steps = dhStepService.getStepsByBpmActivityMetaAndStepBusinessKey(currTaskNode, dhProcessInstance.getInsBusinessKey());
//        nextStep = steps.isEmpty() ? null : steps.get(0);
//
//        dataForSubmitTask = new DataForSubmitTask();
//        dataForSubmitTask.setBpmGlobalConfig(bpmGlobalConfig);
//        dataForSubmitTask.setCurrTaskInstance(systemTaskInstance);
//        dataForSubmitTask.setCurrentProcessInstance(dhProcessInstance);
//        dataForSubmitTask.setPubBo(pubBo);
//        dataForSubmitTask.setBpmRoutingData(bpmRoutingData);
//        dataForSubmitTask.setDhRoutingRecord(routingRecord);
//        dataForSubmitTask.setNextStep(nextStep);
//        dataForSubmitTask.setApplyUser(null); // 系统任务不需要被认领
//        return dataForSubmitTask;
//
//    }


    private List<DhTaskInstance> getTaskListToAutoCommit(BpmGlobalConfig bpmGlobalConfig) {
        DhTaskInstance taskSelective = new DhTaskInstance();
        taskSelective.setUsrUid(bpmGlobalConfig.getBpmAdminName());
        taskSelective.setSynNumber(-1); // synNumer为-1的是要自动提交的任务
        taskSelective.setTaskStatus(DhTaskInstance.STATUS_RECEIVED); // 状态是已收到
        return dhTaskInstanceMapper.selectAllTask(taskSelective);
    }



}