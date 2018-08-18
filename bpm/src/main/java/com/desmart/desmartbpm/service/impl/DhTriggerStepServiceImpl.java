package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.dao.DhTriggerExceptionMapper;
import com.desmart.desmartbpm.entity.DataForSubmitTask;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTriggerException;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.desmart.desmartbpm.service.DhTriggerStepService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DhTriggerStepServiceImpl implements DhTriggerStepService {
    private static final Logger logger = LoggerFactory.getLogger(DhTriggerStepServiceImpl.class);

    @Autowired
    private DhStepService dhStepService;
    @Autowired
    DhTaskInstanceService dhTaskInstanceService;
    @Autowired
    private DhTriggerExceptionMapper dhTriggerExceptionMapper;
    @Autowired
    private DhTriggerService dhTriggerService;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
    @Autowired
    private DhStepMapper dhStepMapper;
    @Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;


    @Override
    public void consumeTriggerMessageFirstTime(Message mqMessage) {
        String msgBody = null;
        DataForSubmitTask dataForSubmitTask = null;
        try {
            msgBody = new String(mqMessage.getBody(), "UTF-8");
            dataForSubmitTask = JSONObject.parseObject(msgBody, new TypeReference<DataForSubmitTask>(){});
        } catch (Exception e) {
            logger.error("TriggerMQConsumer解析消息出错：消息体" + msgBody , e);
            return;
        }
        DhTaskInstance currTaskInstance = dataForSubmitTask.getCurrTaskInstance();
        DhStep firstStep = dataForSubmitTask.getNextStep();
        DhProcessInstance currentProcessInstance = dataForSubmitTask.getCurrentProcessInstance();

        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        //调用触发器方法, 如果是触发器步骤执行，遇到表单步骤略过
        DhStep invokeStep = firstStep;
        while (invokeStep != null) {
            if (DhStep.TYPE_TRIGGER.equals(invokeStep.getStepType()) && StringUtils.isNotBlank(invokeStep.getStepObjectUid())) {
                // 调用触发器
                ServerResponse<Map<String, String>> invokeTriggerResponse = dhTriggerService.invokeTrigger(wac, currentProcessInstance.getInsUid(), invokeStep);
                if (!invokeTriggerResponse.isSuccess()) {
                    // 记录调用异常
                    DhTriggerException dhTriggerException = new DhTriggerException();
                    dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + String.valueOf(UUID.randomUUID()))
                            .setInsUid(currentProcessInstance.getInsUid())
                            .setMqMessage(msgBody)  // mq推送过来的信息的主体内容
                            .setStepUid(invokeStep.getStepUid())
                            .setTaskUid(currTaskInstance.getTaskUid())
                            .setErrorMessage(invokeTriggerResponse.getMsg())   // 记录错误信息
                            .setStatus(DhTriggerException.STATUS_ERROR_IN_STEP); // 记录状态
                    if (invokeTriggerResponse.getStatus() == 2) {
                        // 如果状态码是2，说明是调用接口错误，记录调用接口的日志主键
                        dhTriggerException.setDilUid(invokeTriggerResponse.getData().get("dilUid"));
                    }
                    dhTriggerExceptionMapper.save(dhTriggerException);
                    // 将任务的状态标记为异常
                    dhTaskInstanceMapper.updateTaskStatus(currTaskInstance.getTaskUid(), DhTaskInstance.STATUS_ERROR_AFTER_SUBMIT);
                    // 修改流程实例状态为异常
                    setProcessStatusError(currTaskInstance.getInsUid());
                    return;
                }
            }
            invokeStep = dhStepService.getNextStepOfCurrStep(invokeStep);
        } // 所有步骤执行完成

        // 由于调用了触发器，重新查询一次流程实例信息，因为其中的 nextBusinessKey可能会影响子流程的创建
        currentProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currentProcessInstance.getInsUid());
        dataForSubmitTask.setCurrentProcessInstance(currentProcessInstance);

        try {
            ServerResponse finishResponse = dhTaskInstanceService.finishTask(dataForSubmitTask);
            // 完成任务成功，不需要额外处理
            if (!finishResponse.isSuccess()) {
                // 完成任务失败，记录错误记录
                DhTriggerException dhTriggerException = new DhTriggerException();
                dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + String.valueOf(UUID.randomUUID()))
                        .setTaskUid(currTaskInstance.getTaskUid())
                        .setStepUid(null)
                        .setInsUid(currentProcessInstance.getInsUid())
                        .setMqMessage(msgBody)  // mq推送过来的信息的主体内容
                        .setErrorMessage(finishResponse.getMsg());   // 记录错误信息
                if (finishResponse.getStatus() == 1) {
                    // 调用restfual api 失败
                    dhTriggerException.setStatus(DhTriggerException.STATUS_ERROR_WHEN_SUBMIT);
                } else if (finishResponse.getStatus() == 2) {
                    // 提交成功，判断token是否移动失败
                    dhTriggerException.setStatus(DhTriggerException.STATUS_ERROR_AFTER_SUBMIT);
                }
                dhTriggerExceptionMapper.save(dhTriggerException);
                dhTaskInstanceMapper.updateTaskStatus(currTaskInstance.getTaskUid(), DhTaskInstance.STATUS_ERROR_AFTER_SUBMIT);
            }
        } catch (Exception e) {
            logger.error("步骤执行后，完成任务失败", e);
            DhTriggerException dhTriggerException = new DhTriggerException();
            dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + String.valueOf(UUID.randomUUID()))
                    .setTaskUid(currTaskInstance.getTaskUid())
                    .setStepUid(null)
                    .setInsUid(currentProcessInstance.getInsUid())
                    .setMqMessage(msgBody)  // mq推送过来的信息的主体内容
                    .setErrorMessage("步骤执行后，完成任务失败")
                    .setStatus(DhTriggerException.STATUS_ERROR_AFTER_SUBMIT);
            dhTriggerExceptionMapper.save(dhTriggerException);
            dhTaskInstanceMapper.updateTaskStatus(currTaskInstance.getTaskUid(), DhTaskInstance.STATUS_ERROR_AFTER_SUBMIT);
        }


    }



    @Override
    public ServerResponse retryErrorStepAndSubmitTask(String triggerExceptionId) {
        if (StringUtils.isBlank(triggerExceptionId)) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
        // 获得出错的数据
        DhTriggerException dhTriggerException = dhTriggerExceptionMapper.qureyByPrimaryKey(triggerExceptionId);
        if (dhTriggerException == null) {
            return ServerResponse.createByErrorMessage("找不到这条记录");
        }
        if (DhTriggerException.STATUS_DONE.equals(dhTriggerException.getStatus())) {
            return ServerResponse.createBySuccess("此错误已经被重试成功");
        }

        String msgBody = dhTriggerException.getMqMessage();
        if (StringUtils.isBlank(msgBody)) {
            return ServerResponse.createByErrorMessage("缺少消息体");
        }
        // 初始化
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        DataForSubmitTask dataForSubmitTask = null;
        try {
            dataForSubmitTask = JSONObject.parseObject(msgBody, new TypeReference<DataForSubmitTask>() {});
        } catch (Exception e) {
            logger.error("解析消息出错：消息体" + msgBody, e);
            this.updateTriggerExceptionWhenFailed(dhTriggerException, dhTriggerException.getStepUid(), "解析消息出错：消息体",
                    null, DhTriggerException.STATUS_ERROR_IN_STEP);
        }
        DhTaskInstance currTaskInstance = dataForSubmitTask.getCurrTaskInstance();
        DhProcessInstance currentProcessInstance = dataForSubmitTask.getCurrentProcessInstance();

        // 调用触发器方法, 如果是触发器步骤执行，遇到表单步骤略过
        boolean isTriggerExceptionStep = true; // 记录是记录开始的步骤
        // 从错误步骤开始
        DhStep invokeStep = dhStepMapper.selectByPrimaryKey(dhTriggerException.getStepUid());
        while (invokeStep != null) {
            if (DhStep.TYPE_TRIGGER.equals(invokeStep.getStepType()) && StringUtils.isNotBlank(invokeStep.getStepObjectUid())) {
                ServerResponse<Map<String, String>> invokeTriggerResponse = dhTriggerService.invokeTrigger(wac, currTaskInstance.getInsUid(), invokeStep);
                if (invokeTriggerResponse.isSuccess()) {
                    // 调用触发器成功, 如果当前步骤是错误记录的步骤，则将记录状态更新
                    if (isTriggerExceptionStep) {
                        this.setTriggerExceptionDone(triggerExceptionId);
                    }
                } else {
                    // 调用触发器失败
                    if (invokeTriggerResponse.getStatus() == 1) {
                        updateTriggerExceptionWhenFailed(dhTriggerException, invokeStep.getStepUid(), invokeTriggerResponse.getMsg(),
                                null, DhTriggerException.STATUS_ERROR_IN_STEP);
                    } else if (invokeTriggerResponse.getStatus() == 2) {
                        // 如果状态码是2，说明是调用接口错误，记录调用接口的日志主键
                        updateTriggerExceptionWhenFailed(dhTriggerException, invokeStep.getStepUid(), invokeTriggerResponse.getMsg(),
                                invokeTriggerResponse.getData().get("dilUid"), DhTriggerException.STATUS_ERROR_IN_STEP);
                    }
                    return invokeTriggerResponse; // 终止继续调用
                }
            }
            isTriggerExceptionStep = false;
            invokeStep = dhStepService.getNextStepOfCurrStep(invokeStep);
        } // 所有步骤执行完成
        // 由于调用了触发器，重新查询一次流程实例信息，因为其中的 nextBusinessKey可能会影响走向
        currentProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currentProcessInstance.getInsUid());
        dataForSubmitTask.setCurrentProcessInstance(currentProcessInstance);

        try {
            ServerResponse finishResponse = dhTaskInstanceService.finishTask(dataForSubmitTask);
            if (finishResponse.isSuccess()) {
                // 完成任务成功: 1. 修改异常表中状态  2. 修改任务实例状态 3. 修改流程实例状态
                this.setTriggerExceptionDone(dhTriggerException.getId());
                dhTaskInstanceMapper.updateTaskStatus(currTaskInstance.getTaskUid(), DhTaskInstance.STATUS_CLOSED);
                recoverProcessStatus(currentProcessInstance.getInsUid());
            } else {
                if (finishResponse.getStatus() == 1) {
                    // 调用restfual api 失败
                    updateTriggerExceptionWhenFailed(dhTriggerException, null, finishResponse.getMsg(),
                            null, DhTriggerException.STATUS_ERROR_WHEN_SUBMIT);
                } else if (finishResponse.getStatus() == 2) {
                    // 提交成功，判断token是否移动失败
                    updateTriggerExceptionWhenFailed(dhTriggerException, null, finishResponse.getMsg(),
                            null, DhTriggerException.STATUS_ERROR_AFTER_SUBMIT);
                }
            }
            return finishResponse;
        } catch (Exception e) {
            logger.error("步骤执行完成后提交出错", e);
            this.updateTriggerExceptionWhenFailed(dhTriggerException, null, e.getMessage(), null,
                    DhTriggerException.STATUS_ERROR_AFTER_SUBMIT);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 将指定流程实例状态设为异常
     * @param insUid  流程实例主键
     */
    private void setProcessStatusError(String insUid) {
        if (StringUtils.isNotBlank(insUid)) {
            DhProcessInstance instanceSelective = new DhProcessInstance(insUid);
            instanceSelective.setInsStatusId(DhProcessInstance.STATUS_ID_FAILED); // 设为异常状态
            dhProcessInstanceMapper.updateByPrimaryKeySelective(instanceSelective);
        }
    }

    /**
     * 如果流程实例状态还是异常，并且该流程没有其他异常的任务，将他状态设为正常
     */
    private void recoverProcessStatus(String insUid) {
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        int insStatusId = dhProcessInstance.getInsStatusId();
        if (insStatusId != DhProcessInstance.STATUS_ID_FAILED) {
            return;
        }

        // 检查有没有其他异常的任务
        if (dhTaskInstanceService.listErrorTasksByInsUid(insUid).isEmpty()) {
            DhProcessInstance instanceSelective = new DhProcessInstance(insUid);
            instanceSelective.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE); // 设为正常状态
            dhProcessInstanceMapper.updateByPrimaryKeySelective(instanceSelective);
        }

    }

    /**
     * 执行失败时更新错误记录
     * @param oldTriggerException  被重试的错误记录
     * @param stepUid   发生错误的步骤
     * @param errorMessage   错误信息
     * @param dilUid   接口调用日志主键
     * @param status   错误状态
     */
    private void updateTriggerExceptionWhenFailed(DhTriggerException oldTriggerException, String stepUid, String errorMessage,
                                                  String dilUid, String status) {
        oldTriggerException.setStatus(status)
                .setStepUid(stepUid).setDilUid(dilUid).setErrorMessage(errorMessage).setLastRetryTime(new Date());
        if ((oldTriggerException.getStepUid() == null && stepUid == null)
                || oldTriggerException.getStepUid().equals(stepUid)) {
            // 同一个步骤发生错误，更新重试次数+1
            oldTriggerException.setRetryCount(oldTriggerException.getRetryCount().intValue() + 1);
        } else {
            // 不同步骤发生错误，更新重试次数为0
            oldTriggerException.setRetryCount(0);
        }
        dhTriggerExceptionMapper.updateByPrimaryKey(oldTriggerException);
    }



    /**
     * 将指定异常记录的状态设为完成
     * @param id 异常记录的主键
     */
    private void setTriggerExceptionDone(String id) {
        DhTriggerException exceptionSelective = new DhTriggerException();
        exceptionSelective.setId(id).setLastRetryTime(new Date()).setStepUid(DhTriggerException.STATUS_DONE);
        dhTriggerExceptionMapper.updateByPrimaryKeySelective(exceptionSelective);
    }



}