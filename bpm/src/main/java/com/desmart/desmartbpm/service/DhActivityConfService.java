package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
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

    /**
     * 根据配置主键，批量删除
     * @param actcUidList
     * @return
     */
    int removeByActcUidList(List<String> actcUidList);

    /**
     * 批量插入
     * @param dhActivityConfList
     * @return
     */
    int insertBatch(List<DhActivityConf> dhActivityConfList);
    /**
     * 加载内部通知信息
     * @param dhActivityConf
     * @return
     */
    public ServerResponse loadInteriorNotifyOfActivity(DhActivityConf dhActivityConf);
    /**
     * 加载外部通知信息
     * @param dhActivityConf
     * @return
     */
    public ServerResponse loadExteriorNotifyOfActivity(DhActivityConf dhActivityConf);
}