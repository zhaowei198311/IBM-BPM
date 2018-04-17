package com.desmart.desmart_systemmanagement.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmart_systemmanagement.entity.SysTeam;

@Repository
public interface SysTeamMapper {
    int deleteByPrimaryKey(String teamUid);

    int insert(SysTeam record);

    int insertSelective(SysTeam record);

    SysTeam selectByPrimaryKey(String teamUid);
    
    SysTeam selectByPrimary(SysTeam sysTeam);

    int updateByPrimaryKeySelective(SysTeam record);

    int updateByPrimaryKey(SysTeam record);
    
    public List<SysTeam> selectAll(SysTeam entity);
}