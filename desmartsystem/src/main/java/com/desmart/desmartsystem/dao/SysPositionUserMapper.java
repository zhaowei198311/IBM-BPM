package com.desmart.desmartsystem.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysPositionUser;

@Repository
public interface SysPositionUserMapper {
    int deleteByPrimaryKey(String mapUid);

    int insert(SysPositionUser record);

    int insertSelective(SysPositionUser record);

    SysPositionUser selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysPositionUser record);

    int updateByPrimaryKey(SysPositionUser record);
}