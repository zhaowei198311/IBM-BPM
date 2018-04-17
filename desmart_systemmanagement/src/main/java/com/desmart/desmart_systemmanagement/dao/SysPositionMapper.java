package com.desmart.desmart_systemmanagement.dao;

import com.desmart.desmart_systemmanagement.entity.SysPosition;

public interface SysPositionMapper {
    int insert(SysPosition record);

    int insertSelective(SysPosition record);
}