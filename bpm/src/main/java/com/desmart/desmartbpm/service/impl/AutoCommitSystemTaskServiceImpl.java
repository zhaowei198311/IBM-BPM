package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.FormDataUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.common.util.PropertiesUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.mq.rabbit.MqProducerService;
import com.desmart.desmartbpm.service.AutoCommitSystemTaskService;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.*;
import com.desmart.desmartportal.service.*;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AutoCommitSystemTaskServiceImpl implements AutoCommitSystemTaskService {
    private static final Logger logger = LoggerFactory.getLogger(AutoCommitSystemTaskServiceImpl.class);

    @Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;
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
    private DhStepService dhStepService;
    @Autowired
    private ThreadPoolProvideService threadPoolProvideService;
    @Autowired
    private DhRoutingRecordMapper dhRoutingRecordMapper;
    @Autowired
    private DhProcessInstanceService dhProcessInstanceService;
    @Autowired
    private MqProducerService mqProducerService;

    @Override
    public void startAutoCommitSystemTask() {
        logger.info("开始处理系统任务");
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        // 获得待处理列表
        List<DhTaskInstance> taskList = this.getSystemTaskListToAutoCommit(bpmGlobalConfig);
        for (DhTaskInstance taskInstance : taskList) {
            try {
                this.submitSystemTask(taskInstance, bpmGlobalConfig);
            } catch (Exception e) {
                logger.error("处理系统失败：taskUid：" + taskInstance.getTaskUid(), e);
            }
        }
        logger.info("处理系统任务完成");
    }

    /**
     * 提交系统任务
     * @param currTask 任务实例
     * @param bpmGlobalConfig 全局配置
     */
    @Transactional
    public void submitSystemTask(DhTaskInstance currTask, BpmGlobalConfig bpmGlobalConfig) {
        BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(currTask.getTaskActivityId());
        if (currTaskNode == null) {
            throw new PlatformException("系统任务处理失败，找不到任务节点，taskUid：" + currTask.getTaskUid());
        }
        DhActivityConf currTaskConf = currTaskNode.getDhActivityConf();
        if (!"TRUE".equals(currTaskConf.getActcIsSystemTask())) {
            throw new PlatformException("系统任务处理失败，任务不是系统任务，taskUid：" + currTask.getTaskUid());
        }
        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(currTaskNode.getBpmTaskType())
                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(currTaskNode.getLoopType())) {
            throw new PlatformException("系统任务任务类型异常，不能处理循环任务，taskUid：" + currTask.getTaskUid());
        }
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());
        int insId = dhProcessInstance.getInsId();
        if (dhProcessInstance == null) {
            throw new PlatformException("系统任务处理失败，流程实例不存在");
        }
        JSONObject formDataJson = FormDataUtil.getFormDataJsonFromProcessInstance(dhProcessInstance);
        // 获得下个环节的信息
        BpmRoutingData bpmRoutingData = dhRouteService.getBpmRoutingData(currTaskNode, formDataJson);
        CommonBusinessObject pubBo = new CommonBusinessObject(insId);
        // 装配默认处理人
        dhRouteService.assembleTaskOwnerForSystemTask(currTask, dhProcessInstance, pubBo, bpmRoutingData);
        // 如果下个环节有循环任务（保存/更新）处理人信息
        dhRouteService.saveTaskHandlerOfLoopTask(insId, bpmRoutingData, pubBo);

        // 更新网关决策条件
        if (bpmRoutingData.getGatewayNodes().size() > 0) {
            ServerResponse updateResponse = threadPoolProvideService.updateRouteResult(insId, bpmRoutingData);
            if (!updateResponse.isSuccess()) {
                throw new PlatformException("系统任务处理失败，更新网关决策表失败，taskUid：" + currTask.getTaskUid());
            }
        }

        // 生成流转记录
        DhRoutingRecord routingRecord = dhRoutingRecordService.generateSystemTaskRoutingRecord(currTaskNode,
                currTask, bpmGlobalConfig.getBpmAdminName(), bpmRoutingData);

        // 获得步骤
        List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(currTaskNode, dhProcessInstance.getInsBusinessKey());
        if (steps.isEmpty()) { // 如果没有配置步骤
            //  调用api 完成任务
            BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
            Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTask(currTask.getTaskId(), pubBo);
            Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);
            if (errorMap.get("errorResult") == null) {
                // 判断实际TOKEN是否移动了
                ServerResponse<JSONObject> didTokenMoveResponse = dhRouteService.didTokenMove(insId, bpmRoutingData);
                if (didTokenMoveResponse.isSuccess()) {
                    JSONObject processData = didTokenMoveResponse.getData();
                    if (processData != null) {
                        // 确认Token移动了, 插入流转记录
                        dhRoutingRecordMapper.insert(routingRecord);
                        // 关闭需要结束的流程
                        dhProcessInstanceService.closeProcessInstanceByRoutingData(insId, bpmRoutingData, processData);
                        // 创建需要创建的子流程
                        dhProcessInstanceService.createSubProcessInstanceByRoutingData(dhProcessInstance, bpmRoutingData, pubBo, processData);
                    } else {
                        // 确认Token没有移动, 更新流转信息
                        routingRecord.setActivityTo(null);
                        dhRoutingRecordMapper.insert(routingRecord);
                    }
                } else {
                    logger.error("判断TOKEN是否移动失败，流程实例编号：" + insId + " 任务主键：" + currTask.getTaskUid());
                    throw new PlatformException("判断TOKEN是否移动失败");
                }
            } else {
                throw new PlatformException("调用RESTful API完成任务失败");
            }
        } else {
            // 有步骤, MQ交互, 向队列发出消息
            Map<String, Object> map = new HashMap<>();
            map.put("currProcessInstance", dhProcessInstance);
            map.put("currTaskInstance", currTask);
            map.put("dhStep", steps.get(0));
            map.put("pubBo", pubBo);
            map.put("routingData", bpmRoutingData); // 预判的下个环节信息
            map.put("routingRecord", routingRecord); // 流转记录
            mqProducerService.sendMessage(PropertiesUtil.getProperty("rabbitmq.routingKey.triggerStep", "triggerStepKey"), map);
        }
    }

    /**
     * 获得未处理的系统任务列表
     * @param bpmGlobalConfig
     * @return
     */
    private List<DhTaskInstance> getSystemTaskListToAutoCommit(BpmGlobalConfig bpmGlobalConfig) {
        DhTaskInstance taskSelective = new DhTaskInstance();
        taskSelective.setUsrUid(bpmGlobalConfig.getBpmAdminName());
        taskSelective.setSynNumber(-2); // synNumer为-2的是系统任务
        taskSelective.setTaskStatus(DhTaskInstance.STATUS_RECEIVED); // 状态是已收到
        return dhTaskInstanceMapper.selectAllTask(taskSelective);
    }


}