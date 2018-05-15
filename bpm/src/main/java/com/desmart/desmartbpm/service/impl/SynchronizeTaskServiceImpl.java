package com.desmart.desmartbpm.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.BpmActivityType;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.enginedao.LswTaskMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.engine.GroupAndMember;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.service.SynchronizeTaskService;
import com.desmart.desmartportal.dao.ProcessInstanceMapper;
import com.desmart.desmartportal.entity.ProcessInstance;
import com.desmart.desmartportal.entity.TaskInstance;
import com.desmart.desmartportal.service.TaskInstanceService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class SynchronizeTaskServiceImpl implements SynchronizeTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizeTaskServiceImpl.class);
    
    @Autowired
    private LswTaskMapper lswTaskMapper;
    @Autowired
    private TaskInstanceService taskInstanceService;
    @Autowired
    private ProcessInstanceMapper processInstanceMapper;
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
            List<TaskInstance> taskList = generateUserTask(lswTask, groupInfo);
        }
        
    }
    
    /**
     * 根据引擎中的任务和
     * @param lswTask
     * @param groupInfo
     * @return
     */
    private List<TaskInstance> generateUserTask(LswTask lswTask, Map<Integer, String> groupInfo) {
        List<TaskInstance> taskList = Lists.newArrayList();
        String activityBpdId = lswTask.getCreatedByBpdFlowObjectId();
        Long insId = lswTask.getBpdInstanceId(); // 实例id
        ProcessInstance proInstance = processInstanceMapper.queryByInsId(insId.intValue());
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
        
        //List<String> userUidList = getHandlerList(lswTask);
        
        String loopType = bpmActivityMeta.getLoopType();
        if (BpmActivityType.NONE_LOOP.equalsIgnoreCase(loopType)) {
            // 普通任务
            
            
        } else if (BpmActivityType.SIMPLE_LOOP.equalsIgnoreCase(loopType)) {
            // 简单循环任务
            
        } else if (BpmActivityType.MULTI_INSTANCE_LOOP.equalsIgnoreCase(loopType)) {
            // 多实例任务
            
        }
        
        
        return null;
    }

    private List<LswTask> getNewTasks() {
        int maxTaskId = taskInstanceService.getMaxTaskIdInDb();
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
