package com.desmart.desmartbpm.dao;

import java.util.List;

import com.desmart.desmartbpm.entity.SysRoleResource;

public interface SysRoleResourceMapper {
    int deleteByPrimaryKey(String roleUid);

    int insert(SysRoleResource record);

    int insertSelective(SysRoleResource record);

    SysRoleResource selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysRoleResource record);

    int updateByPrimaryKey(SysRoleResource record);
    
    public List<SysRoleResource> selectAll(SysRoleResource entity);
}