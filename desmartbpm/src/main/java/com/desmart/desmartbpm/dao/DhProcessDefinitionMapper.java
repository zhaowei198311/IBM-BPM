package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhProcessDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhProcessDefinitionMapper {

    int save(DhProcessDefinition dhProcessDefinition);

    List<DhProcessDefinition> listBySelective(DhProcessDefinition dhProcessDefinition);

    int updateByProAppIdAndProUidAndProVerUidSelective(DhProcessDefinition dhProcessDefinition);
    
    List<DhProcessDefinition> listAll();
}