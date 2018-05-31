package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhAgentRecord;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhAgentService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.SysHolidayService;
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
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhAgentService dhAgentService;
    @Autowired
    private DhAgentRecordMapper dhAgentRecordMapper;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private SysHolidayService sysHolidayService;
    @Autowired
    private DhRouteService dhRouteService;
    
    /**
     * 从引擎同步任务
     */
    //@Scheduled(cron = "0/30 * * * * ?")
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
            Map<String, Object> data = null;
            try {
                data =  handleLswTask(lswTask, groupInfo);
            } catch (Exception e) {
               LOG.error("拉取任务时分析任务出错：任务编号" + lswTask.getTaskId(), e);
            }
            if (data != null) {
                dhTaskList.addAll((List<DhTaskInstance>) data.get("dhTaskList"));
                agentRecordList.addAll((List<DhAgentRecord>)data.get("agentRecordList"));
            }
        }
        LOG.info("同步新任务：" + dhTaskList.size() + "个");
        LOG.info("产生代理记录：" + agentRecordList.size() + "个");
//        if (dhTaskList.size() > 0) {
//            dhTaskInstanceService.insertBatch(dhTaskList);
//        }
        for(DhTaskInstance task : dhTaskList) {
            dhTaskInstanceMapper.insertTask(task);
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
        
        // 查看任务表中是否已经有这个任务id的任务
        boolean taskExists = dhTaskInstanceService.isTaskExists(lswTask.getTaskId());
        if (taskExists) {
            dhTaskInstanceMapper.updateSynNumberByTaskId(lswTask.getTaskId(), lswTask.getTaskId());
            return null;
        }
        
        BpmActivityMeta metaSelective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        metaSelective.setActivityBpdId(activityBpdId);
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        BpmActivityMeta bpmActivityMeta = list.get(0);
        // 创建出 DhTaskInstance
        // 引擎分配任务的人，再考虑代理情况
        List<String> orgionUserUidList = getHandlerListOfTask(lswTask, groupInfo);
        
        //  查找上个环节处理人信息
//        ServerResponse<BpmActivityMeta> preActivityResponse = dhRouteService.getPreActivity(proInstance, bpmActivityMeta);
//        BpmActivityMeta preMeta = null;
//        if (preActivityResponse.isSuccess()) {
//            preMeta = preActivityResponse.getData();
//        } else {
//            LOG.error("解析上个环节出错, 任务id: " + lswTask.getTaskId());
//        }
        
        List<DhTaskInstance> dhTaskList = generateDhTaskInstance(lswTask, orgionUserUidList, proInstance, bpmActivityMeta, new BpmActivityMeta());
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
     * 根据条件生成平台中的任务
     */
    private List<DhTaskInstance> generateDhTaskInstance(LswTask lswTask,
            List<String> orgionUserUidList, DhProcessInstance dhProcessInstance, BpmActivityMeta bpmActivityMeta, BpmActivityMeta preMeta) {
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
            } else if (BpmActivityMeta.LOOP_TYPE_SIMPLE_LOOP.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_SIMPLE_LOOP);
            } else if (BpmActivityMeta.LOOP_TYPE_MULTI_INSTANCE_LOOP.equals(loopType)) {
                dhTask.setTaskType(DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP);
            }
            dhTask.setTaskStatus(lswTask.getStatus());
            dhTask.setTaskTitle(bpmActivityMeta.getActivityName());
            dhTask.setInsUpdateDate(dhProcessInstance.getInsUpdateDate());
            dhTask.setTaskInitDate(new Date());
            dhTask.setSynNumber(lswTask.getTaskId());
            dhTask.setTaskPreviousUsrUid(preMeta.getUserUid());
            // 设置
            DhActivityConf conf = bpmActivityMeta.getDhActivityConf();
            if (conf.getActcTime() != null && conf.getActcTimeunit() != null) {
                dhTask.setTaskDueDate(sysHolidayService.calculateDueDate(new Date(), conf.getActcTime(), conf.getActcTimeunit()));
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
    
    /**
     * 从引擎中获得新的任务列表
     * @return
     */
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
    
    
   
}
