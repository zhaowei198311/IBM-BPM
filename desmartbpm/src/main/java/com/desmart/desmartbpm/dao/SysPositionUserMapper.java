package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.SysPositionUser;

public interface SysPositionUserMapper {
    int deleteByPrimaryKey(String mapUid);

    int insert(SysPositionUser record);

    int insertSelective(SysPositionUser record);

    SysPositionUser selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysPositionUser record);

    int updateByPrimaryKey(SysPositionUser record);
}