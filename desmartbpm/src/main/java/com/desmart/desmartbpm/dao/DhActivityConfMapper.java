package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhActivityConf;

public interface DhActivityConfMapper {
    int deleteByPrimaryKey(String actcUid);

    int insert(DhActivityConf dhActivityConf);

    DhActivityConf selectByPrimaryKey(String actcUid);

    int updateByPrimaryKeySelective(DhActivityConf selective);

    int updateByPrimaryKey(DhActivityConf dhActivityConf);
}