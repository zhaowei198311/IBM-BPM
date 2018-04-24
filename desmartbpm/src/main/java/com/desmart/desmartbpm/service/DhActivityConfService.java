package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;

public interface DhActivityConfService {
    
    /**
     * 根据主键获得配置信息
     * @param actcUid
     * @return
     */
    ServerResponse getActivityConfData(String actcUid);
}