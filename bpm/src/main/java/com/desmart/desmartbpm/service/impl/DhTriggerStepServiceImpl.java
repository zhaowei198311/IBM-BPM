package com.desmart.desmartbpm.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.DhTaskCommitException;
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

        DataForSubmitTask dataForSubmitTask = dhTaskException.getDataForSubmitTaskJavaObject();
        if (dataForSubmitTask == null) {
            newTaskException = dhTaskException;
            newTaskException.setErrorMessage("解析用来提交任务的信息出错");
            return ServerResponse.createByErrorCodeAndData(1, newTaskException.getErrorMessage(), newTaskException);
        }

        DhTaskInstance currTaskInstance = dhTaskException.getDhTaskInstance();
        DhProcessInstance currentProcessInstance = dataForSubmitTask.getCurrentProcessInstance();
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
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
                        newTaskException = DhTaskException.createStepTaskException(currTaskInstance, invokeStep.getStepUid(),
                                dhTaskException.getDataForSubmitTask(), invokeTriggerResponse.getMsg(), null);
                    } else if (isInterfaceError(invokeTriggerResponse)) {
                        // 如果状态码是2，说明是调用接口错误，记录调用接口的日志主键
                        newTaskException = DhTaskException.createStepTaskException(currTaskInstance, invokeStep.getStepUid(),
                                dhTaskException.getDataForSubmitTask(), invokeTriggerResponse.getMsg(),
                                invokeTriggerResponse.getData().get("dilUid"));
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
            return dhTaskInstanceService.commitTask(dataForSubmitTask);
        } catch (DhTaskCommitException e) {
            logger.error("步骤执行完成后提交出错", e);
            return ServerResponse.createByErrorCodeAndData(1, e.getMessage(),
                    DhTaskException.createCommitTaskrException(currTaskInstance, dhTaskException.getDataForSubmitTask(), e.getMessage()));
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