package com.desmart.desmart_systemmanagement.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmart_systemmanagement.entity.SysPosition;

@Repository
public interface SysPositionMapper {
    int insert(SysPosition record);

    int insertSelective(SysPosition record);
}