package com.desmart.desmartbpm.service.impl;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhSynTaskRetryMapper;
import com.desmart.desmartbpm.enginedao.LswTaskMapper;
import com.desmart.desmartbpm.enginedao.LswUsrGrpMemXrefMapper;
import com.desmart.desmartbpm.entity.DhSynTaskRetry;
import com.desmart.desmartbpm.entity.LockedTask;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.entity.engine.LswUsrGrpMemXref;
import com.desmart.desmartbpm.mongo.CommonMongoDao;
import com.desmart.desmartbpm.mongo.TaskMongoDao;
import com.desmart.desmartbpm.service.AnalyseLswTaskService;
import com.desmart.desmartbpm.service.SynchronizeTaskService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SynchronizeTaskServiceImpl implements SynchronizeTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizeTaskServiceImpl.class);

    @Autowired
    private LswTaskMapper lswTaskMapper;
    @Autowired
    private LswUsrGrpMemXrefMapper lswUsrGrpMemXrefMapper;
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhSynTaskRetryMapper dhSynTaskRetryMapper;
    @Autowired
    private TaskMongoDao taskMongoDao;
    @Autowired
    private CommonMongoDao commonMongoDao;
    @Autowired
    private AnalyseLswTaskService analyseLswTaskService;


    
    /**
     * 从引擎同步任务
     */
    @Scheduled(cron = "0/8 * * * * ?")
    public void synchronizeTaskFromEngine() {
        LOG.info("==================  开始拉取任务  ===============");
        List<LswTask> newLswTaskList = getNewTasks(); // 获得未同步过的任务
        List<Set> groupIdList = new ArrayList<>();


        Map<Integer, List<String>> groupInfo = getGroupInfo(); // 获得临时组与成员对应关系
        Set<Integer> allLockedTaskIds = getAllLockedTaskIds(); // 获得所有被锁的任务
        newLswTaskList = excludeLockedTasks(newLswTaskList, allLockedTaskIds);
        startFirstSynchronize(newLswTaskList, groupInfo); // 开始同步任务
        LOG.info("==================  拉取任务结束  ===============");
    }

    @Scheduled(cron = "0 * * * * ?")
    public void retrySynchronizeTask() {
        LOG.info("==================  开始重试拉取任务  ===============");
        List<LswTask> newLswTaskList = getNewTasksForRetry(); // 获得未同步过的任务
        Map<Integer, List<String>> groupInfo = getGroupInfo(); // 获得临时组与成员对应关系
        Set<Integer> allLockedTaskIds = getAllLockedTaskIds(); // 获得所有被锁的任务
        newLswTaskList = excludeLockedTasks(newLswTaskList, allLockedTaskIds);
        startRetrySynchronize(newLswTaskList, groupInfo); // 开始同步任务
        LOG.info("==================  重试拉取任务结束  ===============");
    }

    public void SynchronizeTask(int taskId) {
        LOG.info("==================  开始拉取单个任务 ===============");
        List<LswTask> newLswTaskList = new ArrayList<>();
        LswTask lswTask = lswTaskMapper.queryTaskByTaskId(taskId);
        if (lswTask == null) {
            return;
        }
        newLswTaskList.add(lswTask);
        Map<Integer, List<String>> groupInfo = getGroupInfo();
        synchronizeSingleTask(newLswTaskList, groupInfo);
        LOG.info("==================  拉取单个任务结束 ===============");
    }


    public void startFirstSynchronize(List<LswTask> newLswTaskList, Map<Integer, List<String>> groupInfo) {
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        for (LswTask lswTask : newLswTaskList) {
            try {
                // 分析一个引擎任务并
                ServerResponse analyseResponse = analyseLswTaskService.analyseLswTask(lswTask, groupInfo, globalConfig);
                if (!analyseResponse.isSuccess()) {
                    commonMongoDao.remove(String.valueOf(lswTask.getTaskId()), CommonMongoDao.CREATED_TASKS_COLLECTION);
                    saveTaskToRetryTable(lswTask, analyseResponse.getMsg());
                }
            } catch (Exception e) {
                // 发生异常，删除创建任务的信息
                commonMongoDao.remove(String.valueOf(lswTask.getTaskId()), CommonMongoDao.CREATED_TASKS_COLLECTION);
                // 将任务记录到重试表中
                saveTaskToRetryTable(lswTask, e.getMessage());
                LOG.error("拉取任务时分析任务出错：任务编号: " + lswTask.getTaskId(), e);
            }
        }
        // 记录最后一个同步的任务
        updateLastSynchronizedTaskId(newLswTaskList);
        if (newLswTaskList.size() > 0) {
            LOG.info("最后一个任务id：" + newLswTaskList.get(newLswTaskList.size() - 1).getTaskId());
        }
    }



    public void startRetrySynchronize(List<LswTask> newLswTaskList, Map<Integer, List<String>> groupInfo) {
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        for (LswTask lswTask : newLswTaskList) {
            try {
                // 分析一个引擎任务
                ServerResponse analyseResponse = analyseLswTaskService.analyseLswTask(lswTask, groupInfo, globalConfig);
                if (analyseResponse.isSuccess()) {
                    // 更新重试列表，完成此任务的重试拉取
                    completeRetrySynTask(lswTask.getTaskId());
                } else {
                    commonMongoDao.remove(String.valueOf(lswTask.getTaskId()), CommonMongoDao.CREATED_TASKS_COLLECTION);
                    updateRetryCount(lswTask, analyseResponse.getMsg());
                }
            } catch (Exception e) {
                commonMongoDao.remove(String.valueOf(lswTask.getTaskId()), CommonMongoDao.CREATED_TASKS_COLLECTION);
                updateRetryCount(lswTask, e.getMessage());
                LOG.error("拉取任务时分析任务出错：任务编号" + lswTask.getTaskId(), e);
            }
        }
    }


    public void synchronizeSingleTask(List<LswTask> newLswTaskList, Map<Integer, List<String>> groupInfo) {
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        for (LswTask lswTask : newLswTaskList) {
            try {
                // 分析一个引擎任务
                ServerResponse analyseResponse = analyseLswTaskService.analyseLswTask(lswTask, groupInfo, globalConfig);
                if (analyseResponse.isSuccess()) {
                    completeRetrySynTask(lswTask.getTaskId());
                } else {
                    commonMongoDao.remove(String.valueOf(lswTask.getTaskId()), CommonMongoDao.CREATED_TASKS_COLLECTION);
                    saveTaskToRetryTable(lswTask, analyseResponse.getMsg());
                }
            } catch (Exception e) {
                commonMongoDao.remove(String.valueOf(lswTask.getTaskId()), CommonMongoDao.CREATED_TASKS_COLLECTION);
                // 将任务记录到重试表中
                saveTaskToRetryTable(lswTask, e.getMessage());
                LOG.error("拉取任务时分析任务出错：任务编号：" + lswTask.getTaskId(), e);
            }
        }
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
     * 获得组与人的映射关系
     * @return   key: 临时组id<br/>
     *  value: List<String> 工号集合
     */
    private Map<Integer, List<String>> getGroupInfo() {
        List<LswUsrGrpMemXref> lswUsrGrpMemXrefs = lswUsrGrpMemXrefMapper.listAll();

        Map<Integer, List<String>> map = Maps.newHashMap();
        for (LswUsrGrpMemXref lswUsrGrpMemXref : lswUsrGrpMemXrefs) {
            int groupId = lswUsrGrpMemXref.getGroupId();
            String userUid = lswUsrGrpMemXref.getUserUid();
            if (map.get(groupId) == null) {
                List<String> list = new ArrayList<>();
                list.add(userUid);
                map.put(groupId, list);
            } else {
                map.get(groupId).add(userUid);
            }
        }
        return map;
    }

    /**
     * 在重试记录中插入一条记录，如果这个任务id在重试列表中已经存在，则更新重试次数
     * @param lswTask 引擎中的任务
     * @return
     */
    private int saveTaskToRetryTable(LswTask lswTask, String errorMessage) {
        int taskId = lswTask.getTaskId();
        if (dhSynTaskRetryMapper.queryByTaskId(taskId) == null) {
            DhSynTaskRetry dhSynTaskRetry = new DhSynTaskRetry();
            dhSynTaskRetry.setId(EntityIdPrefix.DH_SYN_TASK_RETRY + UUID.randomUUID().toString());
            dhSynTaskRetry.setTaskId(lswTask.getTaskId());
            dhSynTaskRetry.setRetryCount(0);
            dhSynTaskRetry.setStatus(0);
            dhSynTaskRetry.setErrorMessage(errorMessage);
            return dhSynTaskRetryMapper.insert(dhSynTaskRetry);
        } else {
            return dhSynTaskRetryMapper.updateRetryCountByTaskId(lswTask.getTaskId(), errorMessage);
        }
    }

    /**
     * 更新重试次数
     * @param lswTask 平台中的任务
     * @return
     */
    private int updateRetryCount(LswTask lswTask, String errorMessage) {
        return dhSynTaskRetryMapper.updateRetryCountByTaskId(lswTask.getTaskId(), errorMessage);
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
