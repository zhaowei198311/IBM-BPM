package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.DateTimeUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.common.util.TokenInfoUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.DhSynTaskRetryMapper;
import com.desmart.desmartbpm.enginedao.LswTaskMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.mongo.CommonMongoDao;
import com.desmart.desmartbpm.mongo.TaskMongoDao;
import com.desmart.desmartbpm.service.AnalyseLswTaskService;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.dao.DhAgentRecordMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhAgentRecord;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.*;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysEmailUtilBean;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SendEmailService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
@Service
public class AnalyseLswTaskServiceImpl implements AnalyseLswTaskService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyseLswTaskServiceImpl.class);

    @Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
    @Autowired
    private DhProcessInstanceService dhProcessInstanceService;
    @Autowired
    private DhAgentService dhAgentService;
    @Autowired
    private DhAgentRecordMapper dhAgentRecordMapper;
    @Autowired
    private SysHolidayService sysHolidayService;
    @Autowired
    private DhRouteService dhRouteService;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private CommonMongoDao commonMongoDao;

    @Override
    @Transactional
    public ServerResponse analyseLswTask(LswTask lswTask, Map<Integer, String> groupInfo, BpmGlobalConfig globalConfig) {
        System.out.println("开始分析任务编号：" + lswTask.getTaskId());

        int taskId = lswTask.getTaskId();
        int insId = lswTask.getBpdInstanceId().intValue(); // 流程实例id
        String activityBpdId = lswTask.getCreatedByBpdFlowObjectId();// 流程图上的元素id

        // 获得任务所属的流程实例, 并定位环节节点
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(globalConfig);
        HttpReturnStatus processDataResult = bpmProcessUtil.getProcessData(insId);
        if (HttpReturnStatusUtil.isErrorResult(processDataResult)) {
            return ServerResponse.createByErrorMessage("拉取任务失败, 通过RESTful API 获得流程数据失败，实例编号： " + insId);
        }
        JSONObject processData = JSON.parseObject(processDataResult.getMsg());

        TokenInfoUtil tokenUtil = new TokenInfoUtil(taskId, processData);
        String parentProcessTokenId = tokenUtil.getParentTokenId();

        if (tokenUtil.getTokenId() == null) {
            return ServerResponse.createByErrorMessage("拉取任务失败, 通过RESTful API 获得流程数据失败，任务编号： " + lswTask.getTaskId());
        }

        DhProcessInstance dhProcessInstance = null; // 流程实例
        BpmActivityMeta bpmActivityMeta = null; // 任务停留的环节
        if (parentProcessTokenId == null) {
            // 如果父token是null，说明当前任务属于主流程
            dhProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);
            if (dhProcessInstance == null) {
                return ServerResponse.createByErrorMessage("拉取任务失败,找不到流程实例：" + insId + "对应的流程！");
            }
            bpmActivityMeta = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityBpdId, "0",
                    dhProcessInstance.getProVerUid());
        } else {
            // 如果父token是存在，说明当前任务属于子流程
            dhProcessInstance = dhProcessInstanceService.queryByInsIdAndTokenId(insId, parentProcessTokenId);
            if (dhProcessInstance == null) {
                return ServerResponse.createByErrorMessage("拉取任务失败,找不到流程实例：" + insId + "对应的流程！");
            }
            bpmActivityMeta = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityBpdId, dhProcessInstance.getTokenActivityId(),
                    dhProcessInstance.getProVerUid());
        }

        // 查看环节的任务类型
        if (bpmActivityMeta == null) {
            return ServerResponse.createByErrorMessage("找不到任务停留的环节，实例编号：" + insId + "， 任务编号：" + lswTask.getTaskId());
        }

        // 引擎分配任务的人，再考虑代理情况
        List<String> orgionUserUidList = getHandlerListOfTask(lswTask, groupInfo);

        // 查找上个环节处理人信息
        ServerResponse<BpmActivityMeta> preActivityResponse = dhRouteService.getPreActivity(dhProcessInstance, bpmActivityMeta);
        BpmActivityMeta preMeta = null;
        if (preActivityResponse.isSuccess()) {
            preMeta = preActivityResponse.getData();
        } else {
            preMeta = new BpmActivityMeta();
            logger.error("解析上个环节出错, 任务id: " + lswTask.getTaskId());
        }
        // 创建 DhTaskInstance
        List<DhTaskInstance> dhTaskList = generateDhTaskInstance(lswTask, orgionUserUidList, dhProcessInstance,
                bpmActivityMeta, preMeta, globalConfig);
        List<DhAgentRecord> agentRecordList = new ArrayList<>();

        //邮件收件人工号集合
        List<String> sendMailToList = new ArrayList<>();

        // 查看是否允许代理
        if ("TRUE".equals(bpmActivityMeta.getDhActivityConf().getActcCanDelegate())) {
            // 查看这个流程有没有代理人
            for (DhTaskInstance task : dhTaskList) {
                Map<String, String> delegateResult = dhAgentService.getDelegateResult(bpmActivityMeta.getProAppId(), bpmActivityMeta.getBpdId(), task.getUsrUid());
                if (delegateResult != null) {
                    task.setTaskDelegateUser(delegateResult.get("delegateUser"));// 代理人工号
                    task.setTaskDelegateDate(new Date());
                    DhAgentRecord agentRecord = new DhAgentRecord();
                    agentRecord.setAgentDetailId(EntityIdPrefix.DH_AGENT_RECORD + UUID.randomUUID().toString());
                    agentRecord.setAgentId(delegateResult.get("agentId"));
                    agentRecord.setAgentUser(delegateResult.get("delegateUser"));
                    agentRecord.setProName(dhProcessInstance.getProName());
                    agentRecord.setTaskTitle(bpmActivityMeta.getActivityName());
                    agentRecord.setTaskUid(task.getTaskUid());
                    agentRecordList.add(agentRecord);
                    sendMailToList.add(delegateResult.get("delegateUser"));
                }else {
                    sendMailToList.add(task.getUsrUid());
                }
            }
        }

        if (commonMongoDao.getStringValue(String.valueOf(taskId), CommonMongoDao.CREATED_TASKS) != null) {
            // 说明任务已经被插入数据库了
            return ServerResponse.createBySuccess();
        }
        // 批量保存任务
        dhTaskInstanceMapper.insertBatch(dhTaskList);
        commonMongoDao.set(String.valueOf(taskId), DateTimeUtil.dateToStr(new Date()), CommonMongoDao.CREATED_TASKS);
        // 批量保存代理信息
        if (!CollectionUtils.isEmpty(agentRecordList)) {
            dhAgentRecordMapper.insertBatch(agentRecordList);
        }

        try {
            // 查看是否需要邮件通知
            if (Const.Boolean.TRUE.equals(bpmActivityMeta.getDhActivityConf().getActcCanMailNotify())) {
                if (StringUtils.isNotBlank(bpmActivityMeta.getDhActivityConf().getActcMailNotifyTemplate()) && sendMailToList.size() > 0) {
                    //设置邮箱模板以及邮件收件人工号集合
                    SysEmailUtilBean sysEmailUtilBean = new SysEmailUtilBean();
                    sysEmailUtilBean.setNotifyTemplateUid(bpmActivityMeta.getDhActivityConf().getActcMailNotifyTemplate());
                    sysEmailUtilBean.setToUserNoList(sendMailToList);
                    // 发送通知邮件
                    String bpmformsHost = globalConfig.getBpmformsHost();
                    sysEmailUtilBean.setDhTaskInstance(dhTaskList.get(0));
                    sysEmailUtilBean.setBpmformsHost(bpmformsHost);
                    sendEmailService.dhSendEmail(sysEmailUtilBean);
                }
            }
        } catch (Exception e) {
            logger.error("拉取任务时，发送邮件通知异常， 任务ID：" + taskId, e);
        }
        return ServerResponse.createBySuccess();
    }

    /**
     * 根据条件生成平台中的任务
     * @param lswTask  引擎中任务
     * @param orgionUserUidList  引擎中的任务处理人，可能由临时组转换得到
     * @param dhProcessInstance 流程实例
     * @param bpmActivityMeta  任务环节
     * @param preMeta  上个环节
     * @return
     */
    private List<DhTaskInstance> generateDhTaskInstance(LswTask lswTask, List<String> orgionUserUidList,
                                                        DhProcessInstance dhProcessInstance, BpmActivityMeta bpmActivityMeta,
                                                        BpmActivityMeta preMeta, BpmGlobalConfig bpmGlobalConfig) {
        List<DhTaskInstance> taskList = new ArrayList<>();
        // 计算到期时间
        DhActivityConf conf = bpmActivityMeta.getDhActivityConf();
        Double timeAmount = 1.0;
        String timeUnit = "day";
        if (conf.getActcTime() != null && conf.getActcTime().doubleValue() != 1.0) {
            timeAmount = conf.getActcTime();
        }
        if (StringUtils.isNotBlank(conf.getActcTimeunit())) {
            timeUnit = conf.getActcTimeunit();
        }
        Date dueDate = sysHolidayService.calculateDueDate(lswTask.getRcvdDatetime(), timeAmount, timeUnit);

        int synNumber = 0;
        // 判断是否系统任务
        if ("TRUE".equals(conf.getActcIsSystemTask()) && !dhRouteService.isFirstTaskOfSubProcessAndWasRejected(bpmActivityMeta, dhProcessInstance)) {
            if (DhActivityConf.DELAY_TYPE_NONE.equals(conf.getActcDelayType())) { // 进一步判断是否是延时任务
                // 非延时的系统任务
                synNumber = -2;
            } else {
                // 延时任务
                synNumber = -3;
            }
            // 将任务分配给管理员
            orgionUserUidList.remove(0);
            orgionUserUidList.add(bpmGlobalConfig.getBpmAdminName());
        } else {
            // 如果不是系统任务，判断能否自动提交
            if (canTaskBeAutoCommited(orgionUserUidList, bpmActivityMeta, preMeta, dhProcessInstance)) {
                // 将任务分配给管理员
                orgionUserUidList.remove(0);
                orgionUserUidList.add(bpmGlobalConfig.getBpmAdminName());
                synNumber = -1; // 自动提交任务标识
            }
        }

        // 为每个处理人创建任务
        for (String orgionUserUid : orgionUserUidList) {
            DhTaskInstance dhTask = new DhTaskInstance();
            dhTask.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
            dhTask.setInsUid(dhProcessInstance.getInsUid());
            dhTask.setTaskId(lswTask.getTaskId());
            dhTask.setUsrUid(orgionUserUid);
            dhTask.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
            // 设置任务循环类型
            String loopType = bpmActivityMeta.getLoopType();
            if (BpmActivityMeta.LOOP_TYPE_NONE.equals(loopType)) { // 普通任务
                dhTask.setTaskType(DhTaskInstance.TYPE_NORMAL);
            } else if (BpmActivityMeta.LOOP_TYPE_SIMPLE_LOOP.equals(loopType)) { // 简单循环任务
                dhTask.setTaskType(DhTaskInstance.TYPE_SIMPLE_LOOP);
            } else if (BpmActivityMeta.LOOP_TYPE_MULTI_INSTANCE_LOOP.equals(loopType)) { // 多实例循环任务
                dhTask.setTaskType(DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP);
            }
            dhTask.setTaskStatus(lswTask.getStatus());
            dhTask.setTaskTitle(bpmActivityMeta.getActivityName());
            dhTask.setTaskInitDate(lswTask.getRcvdDatetime());
            dhTask.setSynNumber(synNumber != 0 ? synNumber : lswTask.getTaskId());
            dhTask.setTaskPreviousUsrUid(preMeta.getUserUid());
            dhTask.setTaskPreviousUsrUsername(preMeta.getUserName());
            dhTask.setTaskDueDate(dueDate);
            dhTask.setTaskActivityId(bpmActivityMeta.getActivityId());
            taskList.add(dhTask);
        }
        return taskList;
    }



    /**
     * 判断任务是否能自动提交
     * @param orgionUserUidList  原始处理人列表
     * @param bpmActivityMeta 环节
     * @param preMeta 上个环节
     * @param dhProcessInstance 流程实例
     * @return
     */
    private boolean canTaskBeAutoCommited(List<String> orgionUserUidList, BpmActivityMeta bpmActivityMeta,
                                          BpmActivityMeta preMeta, DhProcessInstance dhProcessInstance) {
        if (!"TRUE".equals(bpmActivityMeta.getDhActivityConf().getActcCanAutocommit()) || orgionUserUidList.size() > 1) {
            // 如果本环节不允许自动提交，或者引擎中的处理人不是只有一个人
            return false;
        }
        // 上个环节处理人 是 引擎中的处理人
        if (preMeta == null || !orgionUserUidList.get(0).equals(preMeta.getUserUid())) {
            return false;
        }
        // 下个环节只有一个，
        String activityTo = bpmActivityMeta.getActivityTo();
        if (StringUtils.isBlank(activityTo) || activityTo.contains(",")) {
            return false;
        }
        // 如果上个环节的toActivity中不包含当前环节，说明不是正常提交上来的，不能自动提交
        if (!preMeta.getActivityTo().contains(bpmActivityMeta.getActivityBpdId())) {
            return false;
        }
        // 获得下个节点信息, 下个环节是普通的人员节点
        BpmActivityMeta nextNode = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityTo, bpmActivityMeta.getParentActivityId(),
                bpmActivityMeta.getSnapshotId());
        if (nextNode == null) {
            return false;
        }
        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(nextNode.getBpmTaskType())
                || !BpmActivityMeta.LOOP_TYPE_NONE.equals(nextNode.getLoopType())) {
            return false;
        }
        // 下个环节是有处理人的才能自动提交
        // 计算下个环节的默认处理人， 对下个环节来说，上个环节的处理人就是 orgionUserUidList.get(0)
        JSONObject insDataJson = JSON.parseObject(dhProcessInstance.getInsData());
        List<SysUser> defaultOwnerList = dhRouteService.getDefaultTaskOwnerOfTaskNode(nextNode, orgionUserUidList.get(0), dhProcessInstance,
                insDataJson.getJSONObject("formData"));
        return defaultOwnerList.size() > 0;
    }

    /**
     * 获得引擎中任务的处理人列表
     * @param lswTask  引擎中任务
     * @param groupInfo  引擎中群组信息
     * @return
     */
    private List<String> getHandlerListOfTask(LswTask lswTask, Map<Integer, String> groupInfo) {
        List<String> uidList = Lists.newArrayList();
        if (lswTask.getUserId() != -1) {
            // 引擎中分配到个人
            uidList.add(lswTask.getUserName());
        } else {
            // 引擎中分配给临时组
            Long groupId = lswTask.getGroupId();
            String uidStr = groupInfo.get(groupId.intValue());
            if (StringUtils.isNotBlank(uidStr)) {
                uidList.addAll(Arrays.asList(uidStr.split(",")));
            }
        }
        return uidList;
    }





}