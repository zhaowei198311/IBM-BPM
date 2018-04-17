package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTrigger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhTriggerDao {

    int save(DhTrigger dhTrigger);

    DhTrigger getByPrimaryKey(String triUid);

    /**
     * 根据条件检索
     * @param dhTrigger
     * @return
     */
    List<DhTrigger> searchBySelective(DhTrigger dhTrigger);
}