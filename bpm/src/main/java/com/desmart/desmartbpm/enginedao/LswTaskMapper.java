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
    
    List<LswTask> listNewTasks(int taskId);
    
    /**
     * 获得所有临时组，和组成员的信息, 使用","分隔成员信息
     * @return
     */
    List<GroupAndMember> getGroupInfo();

    /**
     *
     */
    List<LswTask> listTasksByDhSynTaskRetryList(List<DhSynTaskRetry> list);

    
}