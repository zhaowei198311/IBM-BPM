package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTrigger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhTriggerMapper {

    int save(DhTrigger dhTrigger);

    DhTrigger getByPrimaryKey(String triUid);

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
}