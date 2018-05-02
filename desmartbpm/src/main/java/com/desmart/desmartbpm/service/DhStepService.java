package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhStep;

public interface DhStepService {
    
    /**
     * 创建步骤
     * @param dhStep
     * @return
     */
    ServerResponse create(DhStep dhStep);
    
}
