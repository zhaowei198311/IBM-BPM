package com.desmart.desmartbpm.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhActivityConf;

@Repository
public interface DhActivityConfMapper {
    int deleteByPrimaryKey(String actcUid);

    int insert(DhActivityConf dhActivityConf);

    DhActivityConf selectByPrimaryKey(String actcUid);

    int updateByPrimaryKeySelective(DhActivityConf selective);

    int updateByPrimaryKey(DhActivityConf dhActivityConf);
    
    DhActivityConf getByActivityId(String activityId);
    
}