package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.dao.DhTaskExceptionMapper;
import com.desmart.desmartbpm.entity.DataForSubmitTask;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTaskException;
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
import java.util.Map;

@Service
public class DhTriggerStepServiceImpl implements DhTriggerStepService {
    private static final Logger logger = LoggerFactory.getLogger(DhTriggerStepServiceImpl.class);

    @Autowired
    private DhStepService dhStepService;
    @Autowired
    DhTaskInstanceService dhTaskInstanceService;
    @Autowired
    private DhTaskExceptionMapper dhTaskExceptionMapper;
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
                    DhTaskException dhTaskException = null;
                    if (invokeTriggerResponse.getStatus() == 2) {
                        // 如果状态码是2，说明是调用接口错误，记录调用接口的日志主键
                        dhTaskException = DhTaskException.createStepTaskException(currTaskInstance, invokeStep.getStepUid(),
                                msgBody, invokeTriggerResponse.getMsg(), invokeTriggerResponse.getData().get("dilUid"));
                    } else {
                        dhTaskException = DhTaskException.createStepTaskException(currTaskInstance, invokeStep.getStepUid(),
                                msgBody, invokeTriggerResponse.getMsg(), null);
                    }
                    dhTaskExceptionMapper.save(dhTaskException);
                    // 将任务的状态标记为异常
                    dhTaskInstanceMapper.updateTaskStatus(currTaskInstance.getTaskUid(), DhTaskInstance.STATUS_ERROR);
                    // 修改流程实例状态为异常
                    dhProcessInstanceMapper.updateInsStatusIdByInsUid(DhProcessInstance.STATUS_ID_FAILED, currTaskInstance.getInsUid());
                    return;
                }
            }
            invokeStep = dhStepService.getNextStepOfCurrStep(invokeStep);
        } // 所有步骤执行完成

        // 由于调用了触发器，重新查询一次流程实例信息，因为其中的 nextBusinessKey可能会影响子流程的创建
        dataForSubmitTask.setCurrentProcessInstance(dhProcessInstanceMapper.selectByPrimaryKey(currentProcessInstance.getInsUid()));
        dataForSubmitTask.setNextStep(null);

        dhTaskInstanceService.finishTaskFirstTime(dataForSubmitTask);
    }

    @Override
    public ServerResponse retryErrorStep(DhTaskException dhTaskException) {
        DhTaskException newTaskException = null;
        String dataForSubmitTaskStr = dhTaskException.getDataForSubmitTask();
        if (StringUtils.isBlank(dataForSubmitTaskStr)) {
            newTaskException = dhTaskException;
            newTaskException.setErrorMessage("缺少用来提交的JSON数据");
            return ServerResponse.createByErrorCodeAndData(1, "缺少用来提交的JSON数据", newTaskException);
        }
        // 初始化
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        DataForSubmitTask dataForSubmitTask = null;
        try {
            dataForSubmitTask = JSONObject.parseObject(dataForSubmitTaskStr, new TypeReference<DataForSubmitTask>() {});
        } catch (Exception e) {
            logger.error("解析消息出错：消息体" + dataForSubmitTaskStr, e);
            newTaskException = dhTaskException;
            newTaskException.setErrorMessage("解析消息出错");
            return ServerResponse.createByErrorCodeAndData(1, "解析消息出错", newTaskException);
        }

        DhTaskInstance currTaskInstance = dhTaskException.getDhTaskInstance();
        DhProcessInstance currentProcessInstance = dataForSubmitTask.getCurrentProcessInstance();

        // 调用触发器方法, 如果是触发器步骤执行，遇到表单步骤略过
        boolean isFirstStepOfRetry = true; // 记录是记录开始的步骤
        // 从错误步骤开始
        DhStep invokeStep = dhStepMapper.selectByPrimaryKey(dhTaskException.getStepUid());
        while (invokeStep != null) {
            if (DhStep.TYPE_TRIGGER.equals(invokeStep.getStepType()) && StringUtils.isNotBlank(invokeStep.getStepObjectUid())) {
                ServerResponse<Map<String, String>> invokeTriggerResponse = dhTriggerService.invokeTrigger(wac, currTaskInstance.getInsUid(), invokeStep);
                if (invokeTriggerResponse.isSuccess()) {
                    // 调用触发器成功, 如果当前步骤是错误记录的步骤，则将记录状态更新
                    if (isFirstStepOfRetry) {
                        this.setTaskExceptionDone(dhTaskException.getId());
                    }
                } else {
                    // 调用触发器失败
                    if (isCommonError(invokeTriggerResponse)) {
                        newTaskException = DhTaskException.createStepTaskException(currTaskInstance, invokeStep.getStepUid(), dataForSubmitTaskStr,
                                invokeTriggerResponse.getMsg(), null);
                    } else if (isInterfaceError(invokeTriggerResponse)) {
                        // 如果状态码是2，说明是调用接口错误，记录调用接口的日志主键
                        newTaskException = DhTaskException.createStepTaskException(currTaskInstance, invokeStep.getStepUid(), dataForSubmitTaskStr,
                                invokeTriggerResponse.getMsg(), invokeTriggerResponse.getData().get("dilUid"));
                    }
                    return ServerResponse.createByErrorCodeAndData(1, invokeTriggerResponse.getMsg(), newTaskException); // 终止继续调用
                }
            }
            isFirstStepOfRetry = false;
            invokeStep = dhStepService.getNextStepOfCurrStep(invokeStep);
        } // 所有步骤执行完成
        // 由于调用了触发器，重新查询一次流程实例信息，因为其中的 nextBusinessKey可能会影响走向
        currentProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currentProcessInstance.getInsUid());
        dataForSubmitTask.setCurrentProcessInstance(currentProcessInstance);
        dataForSubmitTask.setNextStep(null);

        try {
            dhTaskInstanceService.commitTask(dataForSubmitTask);
            // 完成任务成功: 1. 修改异常表中状态  2. 修改任务实例状态 3. 修改流程实例状态
            return ServerResponse.createBySuccess();
        } catch (Exception e) {
            logger.error("步骤执行完成后提交出错", e);
            return ServerResponse.createByErrorCodeAndData(1, e.getMessage(),
                    DhTaskException.createCommitTaskrException(currTaskInstance, dataForSubmitTaskStr, e.getMessage()));
        }
    }

    private boolean isCommonError(ServerResponse invokeTriggerResponse) {
        return invokeTriggerResponse.getStatus() == 1;
    }

    private boolean isInterfaceError(ServerResponse invokeTriggerResponse) {
        return invokeTriggerResponse.getStatus() == 2;
    }


    /**
     * 将指定异常记录的状态设为完成
     * @param id 异常记录的主键
     */
    private void setTaskExceptionDone(String id) {
        DhTaskException exceptionSelective = new DhTaskException();
        exceptionSelective.setId(id).setLastRetryTime(new Date()).setStatus(DhTaskException.STATUS_DONE);
        dhTaskExceptionMapper.updateByPrimaryKeySelective(exceptionSelective);
    }



}