package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.SysPosition;

public interface SysPositionMapper {
    int insert(SysPosition record);

    int insertSelective(SysPosition record);
}