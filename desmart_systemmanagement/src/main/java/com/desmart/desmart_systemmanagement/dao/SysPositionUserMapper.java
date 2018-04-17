package com.desmart.desmart_systemmanagement.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmart_systemmanagement.entity.SysPositionUser;

@Repository
public interface SysPositionUserMapper {
    int deleteByPrimaryKey(String mapUid);

    int insert(SysPositionUser record);

    int insertSelective(SysPositionUser record);

    SysPositionUser selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysPositionUser record);

    int updateByPrimaryKey(SysPositionUser record);
}