package com.desmart.desmartbpm.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmGlobalConfig;

import java.util.List;

@Repository
public interface BpmGlobalConfigDao {

    List<BpmGlobalConfig> queryActiveConfig();
    
    int deleteByPrimaryKey(String configId);

    int insert(BpmGlobalConfig record);

    int insertSelective(BpmGlobalConfig record);

    BpmGlobalConfig selectByPrimaryKey(String configId);

    int updateByPrimaryKeySelective(BpmGlobalConfig record);

    int updateByPrimaryKey(BpmGlobalConfig record);
}