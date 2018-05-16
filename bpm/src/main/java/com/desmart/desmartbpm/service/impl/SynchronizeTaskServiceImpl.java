package com.desmart.desmartbpm.service.impl;

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

import com.desmart.common.constant.bpm.BpmActivityType;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.enginedao.LswTaskMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.engine.GroupAndMember;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.service.SynchronizeTaskService;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
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
    
    
    /**
     * 从引擎同步任务
     */
    public void synchronizeTaskFromEngine() {
        List<LswTask> newLswTaskList = getNewTasks();
        Map<Integer, String> groupInfo = getGroupInfo();
        generateDhTaskInstance(newLswTaskList, groupInfo);
        
    }
    
    @Transactional
    private void generateDhTaskInstance(List<LswTask> newLswTaskList, Map<Integer, String> groupInfo) {
        for (LswTask lswTask : newLswTaskList) {
            List<DhTaskInstance> taskList = generateUserTask(lswTask, groupInfo);
        }
        
    }
    
    /**
     * 根据引擎中的任务和
     * @param lswTask
     * @param groupInfo
     * @return
     */
    private List<DhTaskInstance> generateUserTask(LswTask lswTask, Map<Integer, String> groupInfo) {
        List<DhTaskInstance> taskList = Lists.newArrayList();
        String activityBpdId = lswTask.getCreatedByBpdFlowObjectId();
        Long insId = lswTask.getBpdInstanceId(); // 实例id
        DhProcessInstance proInstance = dhProcessInstanceMapper.queryByInsId(insId.intValue());
        // 查看环节的任务类型
        if (proInstance == null) {
            return taskList;
        }
        String proAppId = proInstance.getProAppId();
        String proUid = proInstance.getProUid();
        String proVerUid = proInstance.getProVerUid();
        
        BpmActivityMeta metaSelective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        metaSelective.setActivityBpdId(activityBpdId);
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        BpmActivityMeta bpmActivityMeta = list.get(0);
        
        // 引擎分配任务的人，再考虑代理情况
        List<String> orgionUserUidList = getHandlerListOfTask(lswTask, groupInfo);
        
        
        
        List<DhTaskInstance> dhTaskList = generateDhTaskInstance(lswTask, orgionUserUidList, proInstance, bpmActivityMeta);
        
        
        // 查看是否允许代理
        if ("TRUE".equals(bpmActivityMeta.getDhActivityConf().getActcCanDelegate())) {
            // todo
            // 查看这个流程有没有代理人
            
        }
        
        
        
        
        return null;
    }

    /**
     * 根据条件生成引擎中的任务
     * @param lswTask
     * @param orgionUserUidList
     * @param loopType
     * @return
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
                
            } else if (BpmActivityMeta.LOOP_TYPE_MULTI_SIMPLE_LOOP.equals(loopType)) {
                
            } else if (BpmActivityMeta.LOOP_TYPE_MULTI_INSTANCE_LOOP.equals(loopType)) {
                
            }
            
            //dhTask.setTaskType()
            dhTask.setTaskStatus(lswTask.getStatus());
            dhTask.setTaskTitle(bpmActivityMeta.getActivityName());
            dhTask.setInsUpdateDate(dhProcessInstance.getInsUpdateDate());
            dhTask.setTaskInitDate(new Date());
            // 设置
            
            //dhTask.setTaskDueDate()
            //dhTask.setTaskRiskDate()
            // todo
            //dhTask.setTaskPriority()
        }
        
        
        return null;
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
    public static void main(String[] args) {
        String str = "00011";
        List<String> list = Arrays.asList(str.split(","));
        for (String s:list) {
            System.out.println(s);
        }
        System.out.println("END");
    }
}
