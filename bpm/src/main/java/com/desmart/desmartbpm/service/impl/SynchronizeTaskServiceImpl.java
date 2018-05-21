package com.desmart.desmartbpm.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.enginedao.LswTaskMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.engine.GroupAndMember;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.SynchronizeTaskService;
import com.desmart.desmartportal.dao.DhAgentRecordMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhAgentRecord;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhAgentService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class SynchronizeTaskServiceImpl implements SynchronizeTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizeTaskServiceImpl.class);
    
    @Autowired
    private LswTaskMapper lswTaskMapper;
    @Autowired
    private DhTaskInstanceService dhTaskInstanceService;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhAgentService dhAgentService;
    @Autowired
    private DhAgentRecordMapper dhAgentRecordMapper;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    
    /**
     * 从引擎同步任务
     */
    //@Scheduled(cron = "0 * * * * ?")
    public void synchronizeTaskFromEngine() {
        LOG.info("==================  开始拉取任务  ===============");
        List<LswTask> newLswTaskList = getNewTasks();
        Map<Integer, String> groupInfo = getGroupInfo();
        generateDhTaskInstance(newLswTaskList, groupInfo);
        LOG.info("==================  拉取任务结束  ===============");
    }
    
    @Transactional
    private void generateDhTaskInstance(List<LswTask> newLswTaskList, Map<Integer, String> groupInfo) {
        List<DhTaskInstance> dhTaskList = Lists.newArrayList();
        List<DhAgentRecord> agentRecordList = Lists.newArrayList();
        
        for (LswTask lswTask : newLswTaskList) {
            Map<String, Object> data = handleLswTask(lswTask, groupInfo);
            if (data != null) {
                dhTaskList.addAll((List<DhTaskInstance>) data.get("dhTaskList"));
                agentRecordList.addAll((List<DhAgentRecord>)data.get("agentRecordList"));
            }
        }
        LOG.info("同步新任务：" + dhTaskList.size() + "个");
        LOG.info("产生代理记录：" + agentRecordList.size() + "个");
        if (dhTaskList.size() > 0) {
            dhTaskInstanceService.insertBatch(dhTaskList);
        }
        if (agentRecordList.size() > 0) {
            dhAgentRecordMapper.insertBatch(agentRecordList);
        }
        
    }
    
    /**
     * 处理一个引擎任务
     * @param lswTask
     * @param groupInfo
     * @return
     */
    private Map<String, Object> handleLswTask(LswTask lswTask, Map<Integer, String> groupInfo) {
        Map<String, Object> result = Maps.newHashMap();
        
        // 流程图上的元素id
        String activityBpdId = lswTask.getCreatedByBpdFlowObjectId();
        Long insId = lswTask.getBpdInstanceId(); // 流程实例id
        DhProcessInstance proInstance = dhProcessInstanceMapper.queryByInsId(insId.intValue());
        // 查看环节的任务类型
        if (proInstance == null) {
            LOG.error("拉取任务失败,找不到流程实例：" + insId + "对应的流程！");
            return null;
        }
        String proAppId = proInstance.getProAppId();
        String proUid = proInstance.getProUid();
        String proVerUid = proInstance.getProVerUid();
        
        // 查看任务表中是否已经有这个任务id的任务， 避免发起流程环节的任务被同步过来 
        boolean taskExists = dhTaskInstanceService.isTaskExists(lswTask.getTaskId());
        if (taskExists) {
            LOG.error("任务id：" + lswTask.getTaskId() + "的任务发现已经存在，放弃拉取此任务");
            return null;
        }
        
        BpmActivityMeta metaSelective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        metaSelective.setActivityBpdId(activityBpdId);
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        BpmActivityMeta bpmActivityMeta = list.get(0);
        
        // 引擎分配任务的人，再考虑代理情况
        List<String> orgionUserUidList = getHandlerListOfTask(lswTask, groupInfo);
        
        List<DhTaskInstance> dhTaskList = generateDhTaskInstance(lswTask, orgionUserUidList, proInstance, bpmActivityMeta);
        List<DhAgentRecord> agentRecordList = Lists.newArrayList();
        
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
                    agentRecord.setProName(proInstance.getProName());
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
     * 根据条件生成平台中的任务（未对代理做处理）
     */
    private List<DhTaskInstance> generateDhTaskInstance(LswTask lswTask,
            List<String> orgionUserUidList, DhProcessInstance dhProcessInstance, BpmActivityMeta bpmActivityMeta) {
        List<DhTaskInstance> taskList = Lists.newArrayList();
        for (String orgionUserUid : orgionUserUidList) {
            DhTaskInstance dhTask = new DhTaskInstance();
            dhTask.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
            dhTask.setInsUid(dhProcessInstance.getInsUid());
            dhTask.setTaskId(lswTask.getTaskId());
            dhTask.setUsrUid(orgionUserUid);
            dhTask.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
            String loopType = bpmActivityMeta.getLoopType();
            if (BpmActivityMeta.LOOP_TYPE_NONE.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_NORMAL);
            } else if (BpmActivityMeta.LOOP_TYPE_MULTI_SIMPLE_LOOP.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_SIMPLE_LOOP);
            } else if (BpmActivityMeta.LOOP_TYPE_MULTI_INSTANCE_LOOP.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP);
            }
            dhTask.setTaskStatus(lswTask.getStatus());
            dhTask.setTaskTitle(bpmActivityMeta.getActivityName());
            dhTask.setInsUpdateDate(dhProcessInstance.getInsUpdateDate());
            dhTask.setTaskInitDate(new Date());
            
            // 设置
            DhActivityConf conf = bpmActivityMeta.getDhActivityConf();
            if (conf.getActcTime() != null && conf.getActcTimeunit() != null) {
                dhTask.setTaskDueDate(calculateDueDate(new Date(), conf.getActcTime(), conf.getActcTimeunit()));
            }
            taskList.add(dhTask);
        }
        return taskList;
    }

    private List<String> getHandlerListOfTask(LswTask lswTask, Map<Integer, String> groupInfo) {
        List<String> uidList = Lists.newArrayList();
        if (lswTask.getUserId() != -1) {
            uidList.add(lswTask.getUserName());
        } else {
            Long groupId = lswTask.getGroupId();
            String uidStr = groupInfo.get(groupId.intValue());
            if (StringUtils.isNotBlank(uidStr)) {
                uidList.addAll(Arrays.asList(uidStr.split(",")));
            }
            
        }
        return uidList;
    }

    private List<LswTask> getNewTasks() {
        int maxTaskId = dhTaskInstanceService.getMaxTaskIdInDb();
        return lswTaskMapper.listNewTasks(maxTaskId);
    }
    
    private Map<Integer, String> getGroupInfo() {
        List<GroupAndMember> groupInfoList = lswTaskMapper.getGroupInfo();
        Map<Integer, String> map = Maps.newHashMap();
        for (GroupAndMember groupAndMember : groupInfoList) {
            map.put(groupAndMember.getGroupId(), groupAndMember.getMembers());
        }
        return map;
    }
    
    public Date calculateDueDate(Date date, Double timeAmount, String timeUnit) {
        long addAmount = 0;
        if (DhActivityConf.TIME_UNIT_HOUR.equals(timeUnit)) {
            addAmount = (long)(1000L*60*60*timeAmount);
        } else if (DhActivityConf.TIME_UNIT_DAY.equals(timeUnit)) {
            addAmount = (long)(1000L*60*60*24*timeAmount);
        } else if (DhActivityConf.TIME_UNIT_MONTH.equals(timeUnit)) {
            addAmount = (long)(1000L*60*60*24*30*timeAmount);
        }
        return new Date(date.getTime() + addAmount);
    }
    
   
}
