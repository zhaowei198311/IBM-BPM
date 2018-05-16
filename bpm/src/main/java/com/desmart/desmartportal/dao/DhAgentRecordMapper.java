package com.desmart.desmartportal.dao;

import java.util.List;

import com.desmart.desmartportal.entity.DhAgentRecord;

public interface DhAgentRecordMapper {
    int deleteByPrimaryKey(String agentDetailId);

    int insert(DhAgentRecord record);

    int insertSelective(DhAgentRecord record);

    DhAgentRecord selectByPrimaryKey(String agentDetailId);

    int updateByPrimaryKeySelective(DhAgentRecord record);

    int updateByPrimaryKey(DhAgentRecord record);
    
    int insertBatch(List<DhAgentRecord> list);
    
    
    
}