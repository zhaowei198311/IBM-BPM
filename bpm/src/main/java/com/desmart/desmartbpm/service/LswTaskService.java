package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;

/**
 * 操作引擎中的任务
 */
public interface LswTaskService {

    /**
     * 更改引擎表中任务处理人
     * @param taskId  任务编号
     * @param userUid  平台中任务处理人主键
     * @return
     */
    ServerResponse changeOwnerOfLswTask(int taskId, String userUid);


}