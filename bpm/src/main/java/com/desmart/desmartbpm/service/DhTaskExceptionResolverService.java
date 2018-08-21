package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;

/**
 * 任务异常恢复
 */
public interface DhTaskExceptionResolverService {

    /**
     * 恢复一个异常任务
     * @param taskUid
     * @return
     */
    ServerResponse recoverTask(String taskUid);

}