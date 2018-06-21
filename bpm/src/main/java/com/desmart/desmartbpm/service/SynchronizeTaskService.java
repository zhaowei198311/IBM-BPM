package com.desmart.desmartbpm.service;

public interface SynchronizeTaskService {
    /**
     * 从引擎中同步任务
     */
    void synchronizeTaskFromEngine();

    /**
     * 同步重试记录表中未被拉取的任务
     */
    void retrySynchronizeTask();
}
