package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhStep;

/**
 * 步骤持久层
 */
@Repository
public interface DhStepMapper {
    int deleteByPrimaryKey(String stepUid);

    int insert(DhStep record);

    int insertSelective(DhStep record);

    DhStep selectByPrimaryKey(String stepUid);

    int updateByPrimaryKeySelective(DhStep record);

    int updateByPrimaryKey(DhStep record);
    
    List<DhStep> listBySelective(DhStep selective);
}