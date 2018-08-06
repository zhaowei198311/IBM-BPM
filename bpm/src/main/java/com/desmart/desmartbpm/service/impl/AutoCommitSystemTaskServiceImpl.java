package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.*;
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
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private DhTaskInstanceService dhTaskInstanceService;

    @Scheduled(cron = "55 * * * * ?")
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

    @Scheduled(cron = "55 * * * * ?")
    public void startAutoCommitSystemDelayTask() {
        logger.info("开始处理系统延时任务");
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        // 获得待处理列表
        List<DhTaskInstance> taskList = this.getSystemDelayTaskListToAutoCommit(bpmGlobalConfig);
        for (DhTaskInstance taskInstance : taskList) {
            try {
                this.checkSystemDelayTask(taskInstance, bpmGlobalConfig);
            } catch (Exception e) {
                logger.error("处理系统失败：taskUid：" + taskInstance.getTaskUid(), e);
            }
        }
        logger.info("处理系统延时任务完成");
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
        // 修改任务状态
        dhTaskInstanceService.updateDhTaskInstanceWhenFinishTask(currTask, "{}");
        // 判断是否是子流程的第一个节点，如果是第一个节点，就把任务还给流程发起人


        // 获得步骤
        List<DhStep> steps = dhStepService.getStepsByBpmActivityMetaAndStepBusinessKey(currTaskNode, dhProcessInstance.getInsBusinessKey());
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


    public void checkSystemDelayTask(DhTaskInstance currTask, BpmGlobalConfig bpmGlobalConfig) {
        BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(currTask.getTaskActivityId());
        if (currTaskNode == null) {
            throw new PlatformException("系统延时任务处理失败，找不到任务节点，taskUid：" + currTask.getTaskUid());
        }
        DhActivityConf currTaskConf = currTaskNode.getDhActivityConf();
        if (!"TRUE".equals(currTaskConf.getActcIsSystemTask())) {
            throw new PlatformException("系统延时任务处理失败，任务不是系统任务，taskUid：" + currTask.getTaskUid());
        }
        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(currTaskNode.getBpmTaskType())
                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(currTaskNode.getLoopType())) {
            throw new PlatformException("系统延时任务任务类型异常，不能处理循环任务，taskUid：" + currTask.getTaskUid());
        }
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());

        if (DhActivityConf.DELAY_TYPE_NONE.equals(currTaskConf.getActcDelayType())) {
            this.submitSystemTask(currTask, bpmGlobalConfig);
        } else if (DhActivityConf.DELAY_TYPE_TIME.equals(currTaskConf.getActcDelayType())) {
            // 计算是否到达提交时间: 任务接收时间 + 延时时间 < 当前时间 即处理
            if (checkTime(currTask, currTaskConf)) {
                this.submitSystemTask(currTask, bpmGlobalConfig);
            }
        } else if (DhActivityConf.DELAY_TYPE_FIELD.equals(currTaskConf.getActcDelayType())) {
            if (checkTime(currTask, currTaskConf, dhProcessInstance)) {
                this.submitSystemTask(currTask, bpmGlobalConfig);
            }
        } else {
            throw new PlatformException("系统延时任务处理失败，延时类型异常，taskUid：" + currTask.getTaskUid());
        }

    }

    /**
     * 检查现在能否提交任务
     * @param taskInstance  任务实例id
     * @param activityConf  环节配置
     * @return
     */
    private boolean checkTime(DhTaskInstance taskInstance, DhActivityConf activityConf) {
        Date receiveDate = taskInstance.getTaskInitDate();
        DateTime dateTime = new DateTime(receiveDate);
        if (DhActivityConf.TIME_UNIT_HOUR.equals(activityConf.getActcDelayTimeunit())) {
            // 小时
            dateTime = dateTime.plusHours(activityConf.getActcDelayTime());
        } else if (DhActivityConf.TIME_UNIT_DAY.equals(activityConf.getActcDelayTimeunit())) {
            // 天
            dateTime = dateTime.plusDays(activityConf.getActcDelayTime());
        } else {
            // 月
            dateTime = dateTime.plusMonths(activityConf.getActcDelayTime());
        }
        return dateTime.getMillis() > System.currentTimeMillis();
    }

    /**
     * 检查现在能否提交任务
     * @param taskInstance
     * @param activityConf
     * @param dhProcessInstance
     * @return
     */
    private boolean checkTime(DhTaskInstance taskInstance, DhActivityConf activityConf, DhProcessInstance dhProcessInstance) {
        String actcDelayField = activityConf.getActcDelayField();
        if (StringUtils.isBlank(actcDelayField)) {
            throw new PlatformException("描述时间的字段解析失败，缺少该字段");
        }
        JSONObject formDataJson = FormDataUtil.getFormDataJsonFromProcessInstance(dhProcessInstance);
        String timeStr = FormDataUtil.getStringValue(actcDelayField, formDataJson);
        if (StringUtils.isBlank(timeStr)) {
            throw new PlatformException("描述时间的字段解析失败，该字段没有匹配的值");
        }
        Pattern pattern = Pattern.compile("[1-2]\\d{3}-[0-1]{0,1}\\d-[0-3]{0,1}\\d\\s[0-2]{0,1}\\d:[0-5]{0,1}\\d:[0-5]{0,1}\\d");
        Matcher matcher = pattern.matcher(timeStr);
        if (matcher.matches()) {
            // 用 yyyy-MM-dd HH:mm:ss 匹配
            Date date = DateTimeUtil.strToDate(timeStr);
            return date.getTime() < System.currentTimeMillis();
        } else {
            // 用 yyyy-MM-dd 匹配
            pattern = Pattern.compile("[1-2]\\d{3}-[0-1]{0,1}\\d-[0-3]{0,1}\\d");
            matcher = pattern.matcher(timeStr);
            if (matcher.matches()) {
                Date date = DateTimeUtil.strToDate(timeStr, "yyyy-MM-dd");
                return date.getTime() < System.currentTimeMillis();
            } else {
                throw new PlatformException("描述时间的字段解析失败，该字段没有匹配的值");
            }
        }
    }

//    public static void main(String[] args) throws ParseException {
//       DhTaskInstance taskInstance = new DhTaskInstance();
//       taskInstance.setTaskInitDate(DateTimeUtil.strToDate("2018-07-20", "yyyy-MM-dd"));
//       DhActivityConf conf = new DhActivityConf();
//       conf.setActcDelayTime(4);
//       conf.setActcDelayTimeunit("day");
//       conf.setActcDelayField("time");
//       DhProcessInstance dhProcessInstance = new DhProcessInstance();
//       String str = "{ formData: { time: { value: \"2018-07-24\" } } }";
//        dhProcessInstance.setInsData(str);
//        System.out.println(checkTime(taskInstance, conf, dhProcessInstance));
//    }


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


    private List<DhTaskInstance> getSystemDelayTaskListToAutoCommit(BpmGlobalConfig bpmGlobalConfig) {
        DhTaskInstance taskSelective = new DhTaskInstance();
        taskSelective.setUsrUid(bpmGlobalConfig.getBpmAdminName());
        taskSelective.setSynNumber(-3); // synNumer为-3的是系统延时任务
        taskSelective.setTaskStatus(DhTaskInstance.STATUS_RECEIVED); // 状态是已收到
        return dhTaskInstanceMapper.selectAllTask(taskSelective);
    }

    private boolean isFirstTaskNodeOfSubProcess(BpmActivityMeta taskNode, DhProcessInstance currProcessInstance) {
        if (DhProcessInstance.INS_PARENT_OF_MAIN_PROCESS.equals(currProcessInstance.getInsParent())) {
            // 如果是主流程
            return false;
        }
        BpmActivityMeta subProcessNode = bpmActivityMetaService.queryByPrimaryKey(currProcessInstance.getTokenActivityId());
        BpmActivityMeta firstUserTaskMetaOfSubProcess = bpmActivityMetaService.getFirstUserTaskMetaOfSubProcess(subProcessNode);

        return false;
    }

}