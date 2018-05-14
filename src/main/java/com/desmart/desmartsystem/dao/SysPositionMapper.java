package com.desmart.desmartsystem.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysPosition;

@Repository
public interface SysPositionMapper {
    int insert(SysPosition record);

    int insertSelective(SysPosition record);
}