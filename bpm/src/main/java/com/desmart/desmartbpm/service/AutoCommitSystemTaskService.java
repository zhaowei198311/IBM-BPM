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
     * 开始处理系统延时任务
     */
    void startAutoCommitSystemDelayTask();


}