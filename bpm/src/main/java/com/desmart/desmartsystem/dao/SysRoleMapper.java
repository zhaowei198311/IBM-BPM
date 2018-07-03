package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysRole;

@Repository
public interface SysRoleMapper {
    int deleteByPrimaryKey(String roleUid);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(String roleUid);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);
    
    List<SysRole> selectAll(SysRole entity);
    
    List<SysRole> listByPrimaryKeyList(List<String> list);
    
    SysRole select(SysRole entity);
}