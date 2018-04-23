package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhTrigger;

public interface DhTriggerService {

    /**
     * 分页查询符合条件的触发器，按修改时间倒序
     * @param dhTrigger
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse searchTrigger(DhTrigger dhTrigger, Integer pageNum, Integer pageSize);
    
    /**
     * 根据id 删除触发器
     * @param triUid
     * @return
     */
    int deleteTrigger(String triUid);
    
    
    /**
     * 新增触发器
     * @param dhTrigger
     * @return
     */
    int saveTrigger(DhTrigger dhTrigger);

}