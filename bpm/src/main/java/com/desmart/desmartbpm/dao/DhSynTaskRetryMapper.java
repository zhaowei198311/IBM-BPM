package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhSynTaskRetry;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhSynTaskRetryMapper {

    DhSynTaskRetry queryByTaskId(int taskId);

    int insert(DhSynTaskRetry record);

    DhSynTaskRetry selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DhSynTaskRetry record);

    int updateByPrimaryKey(DhSynTaskRetry record);

    /**
     * 列出还有被同步，且尝试次数小于5次的记录
     * @return
     */
    List<DhSynTaskRetry> listUnFinishedTasks();

    /**
     * 根据任务id更新重试次数和最后重试时间
     * @param taskId
     * @return
     */
    int updateRetryCountByTaskId(int taskId);

    /**
     * 成功拉取后的更新
     * @param taskId
     * @return
     */
    int completeRetrySynTask(int taskId);

}