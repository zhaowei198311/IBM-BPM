package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhTaskException;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

/**
 * 处理系统任务
 */
public interface AutoCommitSystemTaskService {

    /**
     * 开始处理系统任务
     */
    void startAutoCommitSystemTask();


    /**
     * 开始处理系统延时任务
     */
    void startAutoCommitSystemDelayTask();

    /**
     * 重试提交系统任务
     * @param dhTaskException
     * @return
     */
    ServerResponse retrySubmitSystemTask(DhTaskException dhTaskException);

}