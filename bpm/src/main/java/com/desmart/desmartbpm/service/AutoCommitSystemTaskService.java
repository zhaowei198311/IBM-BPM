package com.desmart.desmartbpm.service;

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
     * 提交一个系统任务
     * @param currTask
     * @param bpmGlobalConfig
     */
    void submitSystemTask(DhTaskInstance currTask, BpmGlobalConfig bpmGlobalConfig);


}