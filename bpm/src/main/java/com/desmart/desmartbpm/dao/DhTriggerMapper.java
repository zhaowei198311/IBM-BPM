package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTrigger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhTriggerMapper {

    int save(DhTrigger dhTrigger);

    DhTrigger getByPrimaryKey(String triUid);

    /**
     * 根据触发器主键查询触发器
     * @param triggerUidList 触发器主键集合
     * @return
     */
    List<DhTrigger> listByTriggerUidList(List<String> triggerUidList);

    /**
     * 根据条件检索
     * @param dhTrigger
     * @return
     */
    List<DhTrigger> searchBySelective(DhTrigger dhTrigger);
    
    /**
     * 根据触发器id 删除触发器
     * @param triUid
     * @return 
     */
    int delete (String triUid);
    
    /**
     * 修改触发器
     */
    int updateByPrimayKeySelective(DhTrigger dhTrigger);

    /**
     * 获得所有接口类型触发器的主键
     * @return
     */
    List<String> getAllTriUidOfInterfaceTrigger();
}