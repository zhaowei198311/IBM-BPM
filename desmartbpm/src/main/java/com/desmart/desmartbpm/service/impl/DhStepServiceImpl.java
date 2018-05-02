package com.desmart.desmartbpm.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.DhStepService;

@Service
public class DhStepServiceImpl implements DhStepService {

    
    @Override
    public ServerResponse create(DhStep dhStep) {
        if (StringUtils.isBlank(dhStep.getProAppId()) || StringUtils.isBlank(dhStep.getProUid())
                || StringUtils.isBlank(dhStep.getProVerUid()) || StringUtils.isBlank(dhStep.getStepType())) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        } 
        
        
        
        
        return null;
    }
    
    

    
}
