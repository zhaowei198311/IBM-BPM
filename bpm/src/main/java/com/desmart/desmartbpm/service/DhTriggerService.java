package com.desmart.desmartbpm.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.context.WebApplicationContext;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhStep;
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
    ServerResponse deleteTrigger(String triUid);
    
    
    /**
     * 新增触发器
     * @param dhTrigger
     * @return
     */
    ServerResponse saveTrigger(DhTrigger dhTrigger);

    /**
     * 调用触发器
     * @param wac  web容器
     * @param insUid 流程实例主键
     * @param dhStep  步骤
     * @return
     *   map中的参数：
     *   "status": 0 调用成功 1 调用失败
     *   "msg" 错误信息
     *   "param" 出错时的接口参数
     */
    ServerResponse<Map<String, String>> invokeTrigger(WebApplicationContext wac, String insUid, DhStep dhStep);
    
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
    ServerResponse updateTrigger(DhTrigger dhTrigger);

    /**
     * 根据触发器id集合获取触发器
     * @param triggerUidList
     * @return 符合条件的触发器
     */
    List<DhTrigger> listByTriggerUidList(List<String> triggerUidList);

    /**
     * 根据触发器主键查找触发器
     * @param triUid
     * @return
     */
    DhTrigger getTriggerByTriUid(String triUid);

}