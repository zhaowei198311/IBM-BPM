package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;

/**
 * 为引擎的团队过滤提供支持
 */
public interface TeamFilterService {
    
    /**
     * 获取流程实例指定环节的处理人
     * @param insId
     * @param activityBpdId
     * @return
     */
    ServerResponse<String> getHandler(Integer insId, String activityBpdId);
    
}
