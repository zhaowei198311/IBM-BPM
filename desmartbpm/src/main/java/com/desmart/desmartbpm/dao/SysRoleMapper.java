package com.desmart.desmartbpm.dao;

import java.util.List;

import com.desmart.desmartbpm.entity.SysRole;

public interface SysRoleMapper {
    int deleteByPrimaryKey(String roleUid);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(String roleUid);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);
    
    List<SysRole> selectAll(SysRole entity);
}