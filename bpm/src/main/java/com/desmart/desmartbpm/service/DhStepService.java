package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhStep;

public interface DhStepService {
    
    /**
     * 创建步骤
     * @param dhStep
     * @return
     */
    ServerResponse create(DhStep dhStep);
    
    /**
     * 列出DhActivityConf对应的所有步骤
     * @param conf
     * @return
     */
    ServerResponse<List<DhStep>> getStepOfDhActivityConf(DhActivityConf conf);
    
    
    /**
     * 更新步骤
     * @param dhStep
     * @return
     */
    ServerResponse updateStep(DhStep dhStep);
    
    /**
     * 删除步骤
     * @param stepUid
     * @return
     */
    ServerResponse deleteDhStep(String stepUid);
}
