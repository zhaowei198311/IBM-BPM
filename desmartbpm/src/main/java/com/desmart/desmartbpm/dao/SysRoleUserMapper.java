package com.desmart.desmartbpm.dao;

import java.util.List;

import com.desmart.desmartbpm.entity.SysRoleUser;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(String mapUid);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);
    
    public List<SysRoleUser> selectAll(SysRoleUser entity);
}