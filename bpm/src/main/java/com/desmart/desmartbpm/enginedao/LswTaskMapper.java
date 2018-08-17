package com.desmart.desmartbpm.enginedao;

import java.util.List;
import java.util.Map;

import com.desmart.desmartbpm.entity.DhSynTaskRetry;
import com.desmart.desmartbpm.entity.engine.GroupAndMember;
import com.desmart.desmartbpm.entity.engine.LswTask;

/**
 * 流程引擎中任务Dao
 */
public interface LswTaskMapper {

    LswTask selectByPrimaryKey(Integer taskId);

    int updateByPrimaryKeySelective(LswTask record);

    int updateByPrimaryKey(LswTask record);

    /**
     * 从引擎中搜索 任务id大于 指定id的任务， 按任务id从小到大排序
     * @param taskId
     * @return
     */
    List<LswTask> listNewTasks(int taskId);
    


    /**
     * 根据重试记录查询引擎中指定的任务
     * @param list
     * @return
     */
    List<LswTask> listTasksByDhSynTaskRetryList(List<DhSynTaskRetry> list);

    LswTask queryTaskByTaskId(int taskId);
    
}