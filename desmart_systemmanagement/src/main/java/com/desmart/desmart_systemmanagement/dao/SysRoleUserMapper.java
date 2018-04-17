package com.desmart.desmart_systemmanagement.dao;

import java.util.List;

import com.desmart.desmart_systemmanagement.entity.SysRoleUser;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(String mapUid);
    
    int delete(SysRoleUser eneity); 

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);
    
    
    List<SysRoleUser> selectAll(SysRoleUser entity);
}