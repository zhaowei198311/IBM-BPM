package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;

public interface DhActivityConfService {
    
    /**
     * 根据主键获得配置信息
     * @param actcUid
     * @return
     */
    ServerResponse getActivityConfData(String actcUid);
    
    /**
     * 更新环节配置
     * @param dhActivityConf
     * @return
     */
    ServerResponse updateDhActivityConf(DhActivityConf dhActivityConf);
}