package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.*;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.mongo.CommonMongoDao;
import com.desmart.desmartbpm.service.AutoCommitSystemTaskService;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
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

import java.util.Date;
import java.util.List;
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
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhTaskInstanceService dhTaskInstanceService;
    @Autowired
    private CommonMongoDao commonMongoDao;

    @Scheduled(cron = "0/10 * * * * ?")
    @Override
    public void startAutoCommitSystemTask() {
        logger.info("开始处理系统任务");
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        // 获得待处理列表
        List<DhTaskInstance> taskList = this.getSystemTaskListToAutoCommit(bpmGlobalConfig);
        for (DhTaskInstance taskInstance : taskList) {
            this.doFirstSubmit(taskInstance, bpmGlobalConfig);
        }
        logger.info("处理系统任务完成");
    }

    /**
     * 处理系统任务的第一次提交并处理过程
     * @param taskInstance
     * @param bpmGlobalConfig
     * @return
     */
    private ServerResponse doFirstSubmit(DhTaskInstance taskInstance, BpmGlobalConfig bpmGlobalConfig) {
        try {
            ServerResponse submitSystemTaskResponse = dhTaskInstanceService.submitSystemTask(taskInstance, bpmGlobalConfig);
            if (!submitSystemTaskResponse.isSuccess()) {
                // todo 失败时的逻辑
                logger.error("系统任务提交失败", submitSystemTaskResponse.getMsg());
            }
            return submitSystemTaskResponse;
        } catch (Exception e) {
            logger.error("处理系统失败：taskUid：" + taskInstance.getTaskUid(), e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Scheduled(cron = "55 * * * * ?")
    @Override
    public void startAutoCommitSystemDelayTask() {
        logger.info("开始处理系统延时任务");
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        // 获得待处理列表
        List<DhTaskInstance> taskList = this.getSystemDelayTaskListToAutoCommit(bpmGlobalConfig);
        for (DhTaskInstance taskInstance : taskList) {
            try {
                ServerResponse checkResponse = this.checkSystemDelayTask(taskInstance, bpmGlobalConfig);
                if (!checkResponse.isSuccess()) {
                    if (checkResponse.getStatus() != 2) {
                        // 如果出错不是因为未到提交时间
                        // todo 记录到异常表
                    }
                }
            } catch (Exception e) {
                logger.error("处理系统失败：taskUid：" + taskInstance.getTaskUid(), e);
            }
        }
        logger.info("处理系统延时任务完成");
    }


    /**
     * 检查延时任务是否到了提交的时间点, 如果到了提交点就进行提交
     * @param currTask  任务实例
     * @param bpmGlobalConfig  全局配置
     */
    public ServerResponse checkSystemDelayTask(DhTaskInstance currTask, BpmGlobalConfig bpmGlobalConfig) {
        BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(currTask.getTaskActivityId());
        if (currTaskNode == null) {
            return ServerResponse.createByErrorMessage("系统延时任务处理失败，找不到任务节点，taskUid：" + currTask.getTaskUid());
        }
        DhActivityConf currTaskConf = currTaskNode.getDhActivityConf();
        if (!"TRUE".equals(currTaskConf.getActcIsSystemTask())) {
            return ServerResponse.createByErrorMessage("系统延时任务处理失败，任务不是系统任务，taskUid：" + currTask.getTaskUid());
        }
        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(currTaskNode.getBpmTaskType())
                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(currTaskNode.getLoopType())) {
            return ServerResponse.createByErrorMessage("系统延时任务任务类型异常，不能处理循环任务，taskUid：" + currTask.getTaskUid());
        }
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());

        if (DhActivityConf.DELAY_TYPE_NONE.equals(currTaskConf.getActcDelayType())) {
            // 提交系统任务
            return this.doFirstSubmit(currTask, bpmGlobalConfig);
        } else if (DhActivityConf.DELAY_TYPE_TIME.equals(currTaskConf.getActcDelayType())) {
            // 计算是否到达提交时间: 任务接收时间 + 延时时间 < 当前时间 即处理
            if (checkTimeByDelayTime(currTask, currTaskConf)) {
                return this.doFirstSubmit(currTask, bpmGlobalConfig);
            } else {
                return ServerResponse.createByErrorCodeMessage(2, "任务未到提交时间");
            }
        } else if (DhActivityConf.DELAY_TYPE_FIELD.equals(currTaskConf.getActcDelayType())) {
            if (checkTimeByField(currTaskConf, dhProcessInstance)) {
                return this.doFirstSubmit(currTask, bpmGlobalConfig);
            } else {
                return ServerResponse.createByErrorCodeMessage(2, "任务未到提交时间");
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
    private boolean checkTimeByDelayTime(DhTaskInstance taskInstance, DhActivityConf activityConf) {
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
     * @param activityConf
     * @param dhProcessInstance
     * @return
     */
    private boolean checkTimeByField(DhActivityConf activityConf, DhProcessInstance dhProcessInstance) {
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

    /**
     * 获得未处理的系统延迟任务列表
     * @param bpmGlobalConfig
     * @return
     */
    private List<DhTaskInstance> getSystemDelayTaskListToAutoCommit(BpmGlobalConfig bpmGlobalConfig) {
        DhTaskInstance taskSelective = new DhTaskInstance();
        taskSelective.setUsrUid(bpmGlobalConfig.getBpmAdminName());
        taskSelective.setSynNumber(-3); // synNumer为-3的是系统延时任务
        taskSelective.setTaskStatus(DhTaskInstance.STATUS_RECEIVED); // 状态是已收到
        return dhTaskInstanceMapper.selectAllTask(taskSelective);
    }




}