package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhProcessDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhProcessDefinitionDao {

    int save(DhProcessDefinition dhProcessDefinition);

    /**
     * 列出分类下的流程定义
     * @return
     */
    List<DhProcessDefinition> listByProCategory(String categoryUid);

    List<DhProcessDefinition> listBySelective(DhProcessDefinition dhProcessDefinition);

    int updateByProUidAndProVerUidSelective(DhProcessDefinition dhProcessDefinition);
}