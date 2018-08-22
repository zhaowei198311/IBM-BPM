package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhTaskException;

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
     * <p>重试提交系统任务</p>
     * <p>成功时返回status:0</p>
     * <p>失败时ServerResponse包含DhTaskException</p>
     * @param dhTaskException
     * @return
     */
    ServerResponse retrySubmitSystemTask(DhTaskException dhTaskException);

    /**
     * <p>重试提交系统延时任务</p>
     * <p>成功时返回status:0</p>
     * <p>失败时ServerResponse包含DhTaskException</p>
     * @param dhTaskException
     * @return
     */
    ServerResponse retrySubmitSystemDelayTask(DhTaskException dhTaskException);

}