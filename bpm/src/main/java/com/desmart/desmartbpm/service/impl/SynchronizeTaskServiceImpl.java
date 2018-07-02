package com.desmart.desmartbpm.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.ProcessDataUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.DhSynTaskRetryMapper;
import com.desmart.desmartbpm.entity.DhSynTaskRetry;
import com.desmart.desmartbpm.entity.LockedTask;
import com.desmart.desmartbpm.mongo.CommonMongoDao;
import com.desmart.desmartbpm.mongo.TaskMongoDao;
import com.desmart.desmartportal.service.*;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.enginedao.LswTaskMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.engine.GroupAndMember;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.SynchronizeTaskService;
import com.desmart.desmartportal.dao.DhAgentRecordMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhAgentRecord;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.service.SendEmailService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class SynchronizeTaskServiceImpl implements SynchronizeTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizeTaskServiceImpl.class);
    
    @Autowired
    private LswTaskMapper lswTaskMapper;
    @Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;
    @Autowired
    private DhTaskInstanceService dhTaskInstanceService;
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
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhSynTaskRetryMapper dhSynTaskRetryMapper;
    @Autowired
    private TaskMongoDao taskMongoDao;
    @Autowired
    private CommonMongoDao commonMongoDao;

    
    /**
     * 从引擎同步任务
     */
    //@Scheduled(cron = "0/20 * * * * ?")
    public void synchronizeTaskFromEngine() {
        LOG.info("==================  开始拉取任务  ===============");
        List<LswTask> newLswTaskList = getNewTasks(); // 获得未同步过的任务
        Map<Integer, String> groupInfo = getGroupInfo(); // 获得临时组与成员对应关系
        Set<Integer> allLockedTaskIds = getAllLockedTaskIds(); // 获得所有被锁的任务
        newLswTaskList = excludeLockedTasks(newLswTaskList, allLockedTaskIds);
        startFirstSynchronize(newLswTaskList, groupInfo); // 开始同步任务
        LOG.info("==================  拉取任务结束  ===============");
    }

    //@Scheduled(cron = "0 * * * * ?")
    public void retrySynchronizeTask() {
        LOG.info("==================  开始重试拉取任务  ===============");
        List<LswTask> newLswTaskList = getNewTasksForRetry(); // 获得未同步过的任务
        Map<Integer, String> groupInfo = getGroupInfo(); // 获得临时组与成员对应关系
        Set<Integer> allLockedTaskIds = getAllLockedTaskIds(); // 获得所有被锁的任务
        newLswTaskList = excludeLockedTasks(newLswTaskList, allLockedTaskIds);
        startRetrySynchronize(newLswTaskList, groupInfo); // 开始同步任务
        LOG.info("==================  重试拉取任务结束  ===============");
    }

    @Transactional
    public void startFirstSynchronize(List<LswTask> newLswTaskList, Map<Integer, String> groupInfo) {
        List<DhTaskInstance> dhTaskList = new ArrayList<>();
        List<DhAgentRecord> agentRecordList = new ArrayList<>();
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        for (LswTask lswTask : newLswTaskList) {
            Map<String, Object> data = null;
            try {
                // 分析一个引擎任务
                data =  analyseLswTask(lswTask, groupInfo, globalConfig);
            } catch (Exception e) {
                LOG.error("拉取任务时分析任务出错：任务编号" + lswTask.getTaskId(), e);
                // 将任务记录到重试表中
                saveTaskToRetryTable(lswTask);
                continue;
            }
            if (data != null && data.get("dhTaskList") != null) {
                dhTaskList.addAll((List<DhTaskInstance>) data.get("dhTaskList"));
                agentRecordList.addAll((List<DhAgentRecord>)data.get("agentRecordList"));
            }
        }
        for(DhTaskInstance task : dhTaskList) {
        	dhTaskInstanceMapper.insertTask(task);
        	//发送邮件通知
        	//dhSendEmail(task);
        }
        if (agentRecordList.size() > 0) {
            dhAgentRecordMapper.insertBatch(agentRecordList);
        }
        // 记录最后一个同步的任务
        updateLastSynchronizedTaskId(newLswTaskList);
        LOG.info("同步新任务：" + dhTaskList.size() + "个");
        LOG.info("产生代理记录：" + agentRecordList.size() + "个");
    }


    @Transactional
    public void startRetrySynchronize(List<LswTask> newLswTaskList, Map<Integer, String> groupInfo) {
        List<DhTaskInstance> dhTaskList = new ArrayList<>();
        List<DhAgentRecord> agentRecordList = new ArrayList<>();
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        for (LswTask lswTask : newLswTaskList) {
            Map<String, Object> data = null;
            try {
                // 分析一个引擎任务
                data =  analyseLswTask(lswTask, groupInfo, globalConfig);
                completeRetrySynTask(lswTask.getTaskId());
            } catch (Exception e) {
                LOG.error("拉取任务时分析任务出错：任务编号" + lswTask.getTaskId(), e);
                // 更新重试次数
                updateRetryCount(lswTask);
                continue;
            }
            if (data != null && data.get("dhTaskList") != null) {
                dhTaskList.addAll((List<DhTaskInstance>) data.get("dhTaskList"));
                agentRecordList.addAll((List<DhAgentRecord>)data.get("agentRecordList"));
            }
        }
        for(DhTaskInstance task : dhTaskList) {
            dhTaskInstanceMapper.insertTask(task);
            // todo 发送邮件通知
            //dhSendEmail(task);
        }
        if (agentRecordList.size() > 0) {
            dhAgentRecordMapper.insertBatch(agentRecordList);
        }

        LOG.info("重试同步任务产生新任务：" + dhTaskList.size() + "个");
        LOG.info("重试同步任务产生代理记录：" + agentRecordList.size() + "个");
    }
    
	/**
     * 分析引擎中的一个任务，转化为平台任务和代理记录
     * @param lswTask  引擎中的任务
     * @param groupInfo 临时组的信息
     * @param globalConfig  全局配置文件
     * @return
     *   key: dhTaskList  转化后的平台任务列表
     *   key: agentRecordList  这个任务的代理记录
     */
    private Map<String, Object> analyseLswTask(LswTask lswTask, Map<Integer, String> groupInfo, BpmGlobalConfig globalConfig) {
        System.out.println("开始分析任务编号：" + lswTask.getTaskId());
        Map<String, Object> result = new HashMap<>();

        int taskId = lswTask.getTaskId();
        int insId = lswTask.getBpdInstanceId().intValue(); // 流程实例id
        String activityBpdId = lswTask.getCreatedByBpdFlowObjectId();// 流程图上的元素id

        // 获得任务所属的流程实例, 并定位环节节点
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(globalConfig);
        HttpReturnStatus processDataResult = bpmProcessUtil.getProcessData(insId);
        if (HttpReturnStatusUtil.isErrorResult(processDataResult)) {
            throw new PlatformException("拉取任务失败, 通过RESTful API 获得流程数据失败，实例编号： " + insId);
        }
        JSONObject processData = JSON.parseObject(processDataResult.getMsg());

        Map<Object, Object> tokenMap = ProcessDataUtil.getTokenIdAndPreTokenIdByTaskId(taskId, processData);
        if (tokenMap == null || tokenMap.get("tokenId") == null) {
            throw new PlatformException("拉取任务失败, 通过RESTful API 获得流程数据失败，任务编号： " + lswTask.getTaskId());
        }

        DhProcessInstance dhProcessInstance = null; // 流程实例
        BpmActivityMeta bpmActivityMeta = null; // 任务停留的环节
        if (tokenMap.get("preTokenId") == null) {
            // 如果父token是null，说明当前任务属于主流程
            dhProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);
            if (dhProcessInstance == null) {
                throw new PlatformException("拉取任务失败,找不到流程实例：" + insId + "对应的流程！");
            }
            bpmActivityMeta = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityBpdId, "0",
                    dhProcessInstance.getProVerUid());
        } else {
            // 如果父token是存在，说明当前任务属于子流程
            dhProcessInstance = dhProcessInstanceService.queryByInsIdAndTokenId(insId, (String)tokenMap.get("preTokenId"));
            if (dhProcessInstance == null) {
                throw new PlatformException("拉取任务失败,找不到流程实例：" + insId + "对应的流程！");
            }
            bpmActivityMeta = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityBpdId, dhProcessInstance.getTokenActivityId(),
                    dhProcessInstance.getProVerUid());
        }


        // 查看环节的任务类型
        if (bpmActivityMeta == null) {
            throw new PlatformException("找不到任务停留的环节，实例编号：" + insId + "， 任务编号：" + lswTask.getTaskId());
        }
        String proAppId = dhProcessInstance.getProAppId();
        String proUid = dhProcessInstance.getProUid();
        String proVerUid = dhProcessInstance.getProVerUid();
        
        // 查看任务表中是否已经有这个任务id的任务
        boolean taskExists = dhTaskInstanceService.isTaskExists(lswTask.getTaskId());
        if (taskExists) {
            dhTaskInstanceMapper.updateSynNumberByTaskId(lswTask.getTaskId(), lswTask.getTaskId());
            return null;
        }

        // 创建出 DhTaskInstance
        // 引擎分配任务的人，再考虑代理情况
        List<String> orgionUserUidList = getHandlerListOfTask(lswTask, groupInfo);
        
        // 查找上个环节处理人信息
        ServerResponse<BpmActivityMeta> preActivityResponse = dhRouteService.getPreActivity(dhProcessInstance, bpmActivityMeta);
        BpmActivityMeta preMeta = null;
        if (preActivityResponse.isSuccess()) {
            preMeta = preActivityResponse.getData();
        } else {
            preMeta = new BpmActivityMeta();
            LOG.error("解析上个环节出错, 任务id: " + lswTask.getTaskId());
        }
        
        List<DhTaskInstance> dhTaskList = generateDhTaskInstance(lswTask, orgionUserUidList, dhProcessInstance, bpmActivityMeta, preMeta);
        List<DhAgentRecord> agentRecordList = new ArrayList<>();
        

        // 查看是否允许代理
        if ("TRUE".equals(bpmActivityMeta.getDhActivityConf().getActcCanDelegate())) {
            // 查看这个流程有没有代理人
            for (DhTaskInstance task : dhTaskList) {
                Map<String, String> delegateResult = dhAgentService.getDelegateResult(bpmActivityMeta.getProAppId(), bpmActivityMeta.getBpdId(), task.getUsrUid());
                if (delegateResult != null) {
                    task.setTaskDelegateUser(delegateResult.get("delegateUser"));
                    task.setTaskDelegateDate(new Date());
                    DhAgentRecord agentRecord = new DhAgentRecord();
                    agentRecord.setAgentDetailId(EntityIdPrefix.DH_AGENT_RECORD + UUID.randomUUID().toString());
                    agentRecord.setAgentId(delegateResult.get("agentId"));
                    agentRecord.setAgentUser(delegateResult.get("delegateUser"));
                    agentRecord.setProName(dhProcessInstance.getProName());
                    agentRecord.setTaskTitle(bpmActivityMeta.getActivityName());
                    agentRecord.setTaskUid(task.getTaskUid());
                    agentRecordList.add(agentRecord);
                }
            }
        }
        
        result.put("dhTaskList", dhTaskList);
        result.put("agentRecordList", agentRecordList);
        return result;
    }

    /**
     * 根据条件生成平台中的任务
     */
    private List<DhTaskInstance> generateDhTaskInstance(LswTask lswTask,
            List<String> orgionUserUidList, DhProcessInstance dhProcessInstance, BpmActivityMeta bpmActivityMeta, BpmActivityMeta preMeta) {
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
        
        for (String orgionUserUid : orgionUserUidList) {
            DhTaskInstance dhTask = new DhTaskInstance();
            dhTask.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
            dhTask.setInsUid(dhProcessInstance.getInsUid());
            dhTask.setTaskId(lswTask.getTaskId());
            dhTask.setUsrUid(orgionUserUid);
            dhTask.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
            // 设置任务循环类型
            String loopType = bpmActivityMeta.getLoopType();
            if (BpmActivityMeta.LOOP_TYPE_NONE.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_NORMAL);
            } else if (BpmActivityMeta.LOOP_TYPE_SIMPLE_LOOP.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_SIMPLE_LOOP);
            } else if (BpmActivityMeta.LOOP_TYPE_MULTI_INSTANCE_LOOP.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP);
            }
            dhTask.setTaskStatus(lswTask.getStatus());
            dhTask.setTaskTitle(bpmActivityMeta.getActivityName());
            dhTask.setInsUpdateDate(dhProcessInstance.getInsUpdateDate());
            dhTask.setTaskInitDate(lswTask.getRcvdDatetime());
            dhTask.setSynNumber(lswTask.getTaskId());
            dhTask.setTaskPreviousUsrUid(preMeta.getUserUid());
            dhTask.setTaskPreviousUsrUsername(preMeta.getUserName());
            dhTask.setTaskDueDate(dueDate);
            dhTask.setTaskActivityId(bpmActivityMeta.getActivityId());
            taskList.add(dhTask);
        }
        return taskList;
    }

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
    
    /**
     * 从引擎中获得新的任务列表
     * @return
     */
    private List<LswTask> getNewTasks() {
        // 从mongodb中获得最后一次同步的任务编号
        Integer lastTaskIdSynchronized = commonMongoDao.getIntValue(Const.LAST_SYNCHRONIZED_TASK_ID_KEY);
        lastTaskIdSynchronized = lastTaskIdSynchronized == null ? 0 : lastTaskIdSynchronized;
        return lswTaskMapper.listNewTasks(lastTaskIdSynchronized);
    }

    private List<LswTask> getNewTasksForRetry() {
        // 获得表中需要重试的记录
        List<DhSynTaskRetry> dhSynTaskRetries = dhSynTaskRetryMapper.listUnFinishedTasks();
        // 从引擎中获得对应的任务
        if (dhSynTaskRetries.isEmpty()) {
            return new ArrayList<>();
        } else {
            return lswTaskMapper.listTasksByDhSynTaskRetryList(dhSynTaskRetries);
        }
    }

    /**
     * 获得人与临时组的映射关系
     * @return   key: 临时组id, value: 成员1,成员2,成员3...
     */
    private Map<Integer, String> getGroupInfo() {
        List<GroupAndMember> groupInfoList = lswTaskMapper.getGroupInfo();
        Map<Integer, String> map = Maps.newHashMap();
        for (GroupAndMember groupAndMember : groupInfoList) {
            map.put(groupAndMember.getGroupId(), groupAndMember.getMembers());
        }
        return map;
    }


    private void dhSendEmail(DhTaskInstance task) {
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(task.getInsUid());
        String proAppId = dhProcessInstance.getProAppId();
        String activityBpdId = task.getActivityBpdId();
        String snapshotId = dhProcessInstance.getProVerUid();
        String bpdId = dhProcessInstance.getProUid();
        BpmActivityMeta bpmActivityMeta = bpmActivityMetaService.getBpmActivityMeta(proAppId, activityBpdId, snapshotId, bpdId);
        if(Const.Boolean.TRUE.equals(bpmActivityMeta.getDhActivityConf().getActcCanMailNotify())) {
            List<String> toList = new ArrayList<>();
            if(task.getUsrUid()!=null&&!"".equals(task.getUsrUid())) {
                toList.add(task.getUsrUid());
            }
            if(task.getTaskDelegateUser()!=null&&!"".equals(task.getTaskDelegateUser())) {
                toList.add(task.getTaskDelegateUser());
            }
            for (String to : toList) {
                String subject = "邮件通知";
                String body = "邮件通知";
                sendEmailService.sendingEmail(to, subject, body);
            }
        }
    }

    /**
     * 在重试记录中插入一条记录
     * @param lswTask 引擎中的任务
     * @return
     */
    private int saveTaskToRetryTable(LswTask lswTask) {
        DhSynTaskRetry dhSynTaskRetry = new DhSynTaskRetry();
        dhSynTaskRetry.setId(EntityIdPrefix.DH_SYN_TASK_RETRY + UUID.randomUUID().toString());
        dhSynTaskRetry.setTaskId(lswTask.getTaskId());
        dhSynTaskRetry.setRetryCount(0);
        dhSynTaskRetry.setStatus(0);
        return dhSynTaskRetryMapper.insert(dhSynTaskRetry);
    }

    /**
     * 更新重试次数
     * @param lswTask 平台中的任务
     * @return
     */
    private int updateRetryCount(LswTask lswTask) {
        return dhSynTaskRetryMapper.updateRetryCountByTaskId(lswTask.getTaskId());
    }

    /**
     * 更新retryTask的状态为完成
     */
    private int completeRetrySynTask(int taskId) {
        return dhSynTaskRetryMapper.completeRetrySynTask(taskId);
    }

    /**
     * 获得所有被锁住的任务
     * @return
     */
    private Set<Integer> getAllLockedTaskIds() {
        Set<Integer> allLockedTaskIds = new HashSet<>();
        List<LockedTask> allLockedTasks = taskMongoDao.getAllLockedTasks();
        for (LockedTask lockedTask : allLockedTasks) {
            allLockedTaskIds.add(lockedTask.getTaskId());
        }
        return allLockedTaskIds;
    }

    /**
     * 从查到的任务中去除被锁的任务
     * @param taskList
     * @param allLockedTaskIds
     * @return
     */
    private List<LswTask> excludeLockedTasks(List<LswTask> taskList, Set<Integer> allLockedTaskIds) {
        Iterator<LswTask> iterator = taskList.iterator();
        while (iterator.hasNext()) {
            LswTask lswTask = iterator.next();
            if (allLockedTaskIds.contains(lswTask.getTaskId())) {
                iterator.remove();
            }
        }
        return taskList;
    }

    /**
     * 更新同步程序最后同步的任务id
     * @param synchronizedTaskList
     */
    private void updateLastSynchronizedTaskId(List<LswTask> synchronizedTaskList) {
        if (synchronizedTaskList == null || synchronizedTaskList.isEmpty()) {
            return;
        }
        LswTask lastSynTask = synchronizedTaskList.get(synchronizedTaskList.size() - 1);
        commonMongoDao.set(Const.LAST_SYNCHRONIZED_TASK_ID_KEY, lastSynTask.getTaskId());
    }
}
