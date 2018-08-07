package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.DhTriggerExceptionMapper;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTriggerException;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.desmart.desmartbpm.service.DhTriggerStepService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.entity.*;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

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
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhProcessInstanceService dhProcessInstanceService;
    @Autowired
    private DhTriggerExceptionMapper dhTriggerExceptionMapper;
    @Autowired
    private DhRouteService dhRouteServiceImpl;
    @Autowired
    private DhTriggerService dhTriggerService;
    @Autowired
    private DhRoutingRecordMapper dhRoutingRecordMapper;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;

    @Transactional
    @Override
    public void consumeTriggerMessageFirstTime(Message mqMessage) {
        // 初始化
        DhTaskInstance dhTaskInstance = null;
        DhStep dhStep = null;
        DhProcessInstance dhProcessInstance = null;
        CommonBusinessObject pubBo = null;
        BpmRoutingData routingData = null;
        DhRoutingRecord dhRoutingRecord = null;
        String msgBody = null;
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        //jsonObject转javaBean对象
        try {
            msgBody = new String(mqMessage.getBody(), "UTF-8");
            JSONObject json = JSONObject.parseObject(msgBody);
            // String -> jsonObject  解析数据
            JSONObject taskInstanceJson = json.getJSONObject("currTaskInstance");
            JSONObject dhStepJson = json.getJSONObject("dhStep");
            JSONObject processInstanceJson = json.getJSONObject("currProcessInstance");
            JSONObject pubBoJson = json.getJSONObject("pubBo");// CommonBusinessObject
            JSONObject routingDataJson = json.getJSONObject("routingData");
            JSONObject routingRecordJson = json.getJSONObject("routingRecord");
            // josnObject -> JavaBean
            dhTaskInstance = JSONObject.toJavaObject(taskInstanceJson, DhTaskInstance.class);
            dhStep = JSONObject.toJavaObject(dhStepJson, DhStep.class);
            dhProcessInstance = JSONObject.toJavaObject(processInstanceJson, DhProcessInstance.class);
            pubBo = JSONObject.toJavaObject(pubBoJson, CommonBusinessObject.class);
            // 复杂对象特别处理
            routingData = JSONObject.parseObject(routingDataJson.toString(), new TypeReference<BpmRoutingData>(){});
            dhRoutingRecord = JSONObject.toJavaObject(routingRecordJson, DhRoutingRecord.class);
        } catch (Exception e) {
            logger.error("TriggerMQConsumer解析消息出错：消息体" + msgBody , e);
            if (dhTaskInstance != null) {
                setProcessStatusError(dhTaskInstance.getInsUid());
            }
        }

        //调用触发器方法, 如果是触发器步骤执行，遇到表单步骤略过
        DhStep step = dhStep;
        while (step != null) {
            if (DhStep.TYPE_TRIGGER.equals(step.getStepType()) && StringUtils.isNotBlank(step.getStepObjectUid())) {
                ServerResponse<Map<String, String>> invokeResponse = dhTriggerService.invokeTrigger(wac, dhProcessInstance.getInsUid(), step);
                Map<String,String> invokeResult = invokeResponse.getData();
                if(invokeResult.get("status").equals("1")) {
                    // status == 1 表示调用出现异常
                    DhTriggerException dhTriggerException = new DhTriggerException();
                    dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + UUID.randomUUID());
                    dhTriggerException.setInsId(String.valueOf(dhProcessInstance.getInsId()));
                    dhTriggerException.setMqMessage(msgBody);  // mq推送过来的信息的主体内容
                    dhTriggerException.setStepId(dhStep.getStepUid());
                    dhTriggerException.setTaskId(String.valueOf(dhTaskInstance.getTaskId()));
                    dhTriggerException.setTaskUid(dhTaskInstance.getTaskUid());
                    dhTriggerException.setRequestParam(invokeResult.get("param")); // 记录调用参数
                    dhTriggerException.setErrorMessage(invokeResult.get("msg"));   // 记录错误信息
                    dhTriggerException.setStatus(DhTriggerException.STATUS_ERROR); // 记录状态
                    dhTriggerExceptionMapper.save(dhTriggerException);
                    // 修改流程实例状态为异常
                    setProcessStatusError(dhTaskInstance.getInsUid());
                    return;
                }
            }
            step = dhStepService.getNextStepOfCurrStep(step);
        }

        //  调用api 完成任务
        int taskId = dhTaskInstance.getTaskId();
        int insId = dhProcessInstance.getInsId();
        // 由于调用了触发器，重新查询一次流程实例信息
        dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());

        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
        Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTaskWithOutUserInSession(taskId, pubBo);
        Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);
        if (errorMap.get("errorResult") == null) {
            // 判断实际TOKEN是否移动了
            ServerResponse<JSONObject> didTokenMoveResponse = dhRouteServiceImpl.didTokenMove(insId, routingData);
            if (didTokenMoveResponse.isSuccess()) {
                JSONObject processData = didTokenMoveResponse.getData();
                if (processData != null) {
                    // 实际Token移动了
                    dhRoutingRecordMapper.insert(dhRoutingRecord);
                    // 关闭需要结束的流程
                    dhProcessInstanceService.closeProcessInstanceByRoutingData(insId, routingData, processData);
                    // 创建需要创建的子流程
                    dhProcessInstanceService.createSubProcessInstanceByRoutingData(dhProcessInstance, routingData, pubBo, processData);
                } else {
                    // 实际Token没有移动,可能是并行的任务没有被完成， 更新流转信息，去掉to的部分
                    dhRoutingRecord.setActivityTo(null);
                    dhRoutingRecordMapper.insert(dhRoutingRecord);
                }
            } else {
                logger.error("判断TOKEN是否移动失败，流程实例编号：" + insId + " 任务主键：" + dhTaskInstance.getInsUid());
            }
        } else {
            throw new PlatformException("调用RESTful API完成任务失败");
        }
    }

    @Transactional
    @Override
    public ServerResponse retryErrorStepAndSubmitTask(String triggerExceptionId) {
        if (StringUtils.isBlank(triggerExceptionId)) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
        // 获得出错的数据
        DhTriggerException triggerException = dhTriggerExceptionMapper.qureyByPrimaryKey(triggerExceptionId);
        if (triggerException == null) {
            return ServerResponse.createByErrorMessage("找不到这条记录");
        }
        if (!DhTriggerException.STATUS_ERROR.equals(triggerException.getStatus())) {
            return ServerResponse.createByErrorMessage("此错误已经被重试成功");
        }

        String msgBody = triggerException.getMqMessage();
        if (StringUtils.isBlank(msgBody)) {
            return ServerResponse.createByErrorMessage("缺少消息体");
        }
        // 初始化
        DhTaskInstance dhTaskInstance = null;
        DhStep dhStep = null;
        DhProcessInstance dhProcessInstance = null;
        CommonBusinessObject pubBo = null;
        BpmRoutingData routingData = null;
        DhRoutingRecord dhRoutingRecord = null;
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        //jsonObject转javaBean对象
        try {
            JSONObject json = JSONObject.parseObject(msgBody);
            // String -> jsonObject  解析数据
            JSONObject taskInstanceJson = json.getJSONObject("currTaskInstance");
            JSONObject dhStepJson = json.getJSONObject("dhStep");
            JSONObject processInstanceJson = json.getJSONObject("currProcessInstance");
            JSONObject pubBoJson = json.getJSONObject("pubBo");// CommonBusinessObject
            JSONObject routingDataJson = json.getJSONObject("routingData");
            JSONObject routingRecordJson = json.getJSONObject("routingRecord");
            // josnObject -> JavaBean
            dhTaskInstance = JSONObject.toJavaObject(taskInstanceJson, DhTaskInstance.class);
            dhStep = JSONObject.toJavaObject(dhStepJson, DhStep.class);
            dhProcessInstance = JSONObject.toJavaObject(processInstanceJson, DhProcessInstance.class);
            pubBo = JSONObject.toJavaObject(pubBoJson, CommonBusinessObject.class);
            // 复杂对象特别处理
            routingData = JSONObject.parseObject(routingDataJson.toString(), new TypeReference<BpmRoutingData>(){});
            dhRoutingRecord = JSONObject.toJavaObject(routingRecordJson, DhRoutingRecord.class);
        } catch (Exception e) {
            logger.error("重试，TriggerMQConsumer解析消息出错：消息体" + msgBody , e);
            if (dhTaskInstance != null) {
                setProcessStatusError(dhTaskInstance.getInsUid());
            }
            return ServerResponse.createByErrorMessage("重试TriggerStep时，解析消息出错");
        }

        // 调用触发器方法, 如果是触发器步骤执行，遇到表单步骤略过
        DhStep step = dhStep;
        while (step != null) {
            if (DhStep.TYPE_TRIGGER.equals(step.getStepType()) && StringUtils.isNotBlank(step.getStepObjectUid())) {
                ServerResponse<Map<String, String>> invokeResponse = dhTriggerService.invokeTrigger(wac, dhProcessInstance.getInsUid(), step);
                Map<String,String> invokeResult = invokeResponse.getData();
                if(invokeResult.get("status").equals("1")) {
                    // status == 1 表示调用出现异常
                    DhTriggerException dhTriggerException = new DhTriggerException();
                    dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + UUID.randomUUID());
                    dhTriggerException.setInsId(String.valueOf(dhProcessInstance.getInsId()));
                    dhTriggerException.setMqMessage(msgBody);  // mq推送过来的信息的主体内容
                    dhTriggerException.setStepId(dhStep.getStepUid());
                    dhTriggerException.setTaskId(String.valueOf(dhTaskInstance.getTaskId()));
                    dhTriggerException.setRequestParam(invokeResult.get("param")); // 记录调用参数
                    dhTriggerException.setErrorMessage(invokeResult.get("msg"));   // 记录错误信息
                    dhTriggerException.setStatus(DhTriggerException.STATUS_ERROR); // 记录状态
                    this.saveOrUpdateTriggerExceptionWhenFail(dhTriggerException);
                    // 修改流程实例状态为异常
                    setProcessStatusError(dhTaskInstance.getInsUid());
                    return ServerResponse.createByErrorMessage("重试触发器失败");
                }
            }
            step = dhStepService.getNextStepOfCurrStep(step);
        }

        //  调用api 完成任务
        int taskId = dhTaskInstance.getTaskId();
        int insId = dhProcessInstance.getInsId();
        // 由于调用了触发器，重新查询一次流程实例信息
        dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhProcessInstance.getInsUid());

        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
        Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTaskWithOutUserInSession(taskId, pubBo);
        Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);
        if (errorMap.get("errorResult") == null) {
            // 判断实际TOKEN是否移动了
            ServerResponse<JSONObject> didTokenMoveResponse = dhRouteServiceImpl.didTokenMove(insId, routingData);
            if (didTokenMoveResponse.isSuccess()) {
                JSONObject processData = didTokenMoveResponse.getData();
                if (processData != null) {
                    // 实际Token移动了
                    dhRoutingRecordMapper.insert(dhRoutingRecord);
                    // 关闭需要结束的流程
                    dhProcessInstanceService.closeProcessInstanceByRoutingData(insId, routingData, processData);
                    // 创建需要创建的子流程
                    dhProcessInstanceService.createSubProcessInstanceByRoutingData(dhProcessInstance, routingData, pubBo, processData);
                } else {
                    // 实际Token没有移动,可能是并行的任务没有被完成， 更新流转信息，去掉to的部分
                    dhRoutingRecord.setActivityTo(null);
                    dhRoutingRecordMapper.insert(dhRoutingRecord);
                }
                // 恢复流程实例状态为正常
                recoverProcessStatus(dhProcessInstance.getInsUid());
                return ServerResponse.createBySuccess();
            } else {
                logger.error("判断TOKEN是否移动失败，流程实例编号：" + insId + " 任务主键：" + dhTaskInstance.getInsUid());
            }
        } else {
            throw new PlatformException("调用RESTful API完成任务失败");
        }
        return ServerResponse.createByErrorMessage("重试失败");
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
     * 如果流程实例状态还是异常，将他状态设为正常，如果是已完成的不需要改状态
     */
    private void recoverProcessStatus(String insUid) {
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        if (DhProcessInstance.STATUS_ID_FAILED == dhProcessInstance.getInsStatusId()) {
            DhProcessInstance instanceSelective = new DhProcessInstance(insUid);
            instanceSelective.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE); // 设为正常状态
            dhProcessInstanceMapper.updateByPrimaryKeySelective(instanceSelective);
        }
    }

    /**
     * 有这条记录就重试计数+1
     * 没有这条记录就插入
     * @param dhTriggerException
     */
    private void saveOrUpdateTriggerExceptionWhenFail(DhTriggerException dhTriggerException) {
        DhTriggerException exceptionInDb = dhTriggerExceptionMapper.queryByTaskUidAndStepUid(dhTriggerException.getTaskUid(),
                dhTriggerException.getStepId());
        if (exceptionInDb == null) {
            dhTriggerExceptionMapper.save(dhTriggerException);
        } else {
            dhTriggerException.setRetryCount(exceptionInDb.getRetryCount().intValue() + 1);
            dhTriggerExceptionMapper.updateByPrimaryKeySelective(dhTriggerException);
        }
    }

}