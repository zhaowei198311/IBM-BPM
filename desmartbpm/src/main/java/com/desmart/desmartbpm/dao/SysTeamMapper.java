package com.desmart.desmartbpm.dao;

import java.util.List;

import com.desmart.desmartbpm.entity.SysTeam;

public interface SysTeamMapper {
    int deleteByPrimaryKey(String teamUid);

    int insert(SysTeam record);

    int insertSelective(SysTeam record);

    SysTeam selectByPrimaryKey(String teamUid);

    int updateByPrimaryKeySelective(SysTeam record);

    int updateByPrimaryKey(SysTeam record);
    
    public List<SysTeam> selectAll(SysTeam entity);
}