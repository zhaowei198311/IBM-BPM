package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysRoleResource;

@Repository
public interface SysRoleResourceMapper {
    int deleteByPrimaryKey(String roleUid);

    int insert(SysRoleResource record);

    int insertSelective(SysRoleResource record);

    SysRoleResource selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysRoleResource record);

    int updateByPrimaryKey(SysRoleResource record);
    
    public List<SysRoleResource> selectAll(SysRoleResource entity);
}