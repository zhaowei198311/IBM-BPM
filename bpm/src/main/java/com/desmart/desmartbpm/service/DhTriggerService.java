package com.desmart.desmartbpm.service;

import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.desmart.common.constant.ServerResponse;
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
     * 根据主键查询触发器 
     */
    ServerResponse getTriggerByPrimarkey(String triUid);
    
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
    /**
     * @Title: invokeTrigger  
     * @Description: 通过反射调用类的方法  
     * @param @param triUid
     * @param @return  
     * @return DhTrigger
     * @throws
     */
    void invokeTrigger(WebApplicationContext wac, String insUid, String triUid) throws Exception;
    
    /**
     * 反射调用选人触发器
     * @param wac
     * @param insUid
     * @param triUid
     * @return
     */
    public ServerResponse<List<String>> invokeChooseUserTrigger(WebApplicationContext wac, String insUid, String triUid);
    
    /**
     * 触发器修改
     */
    int updateTrigger(DhTrigger dhTrigger);
}