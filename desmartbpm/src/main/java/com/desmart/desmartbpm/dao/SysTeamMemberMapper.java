package com.desmart.desmartbpm.dao;

import java.util.List;

import com.desmart.desmartbpm.entity.SysTeam;
import com.desmart.desmartbpm.entity.SysTeamMember;

public interface SysTeamMemberMapper {
    int deleteByPrimaryKey(String memberUid);

    int insert(SysTeamMember record);

    int insertSelective(SysTeamMember record);

    SysTeamMember selectByPrimaryKey(String memberUid);

    int updateByPrimaryKeySelective(SysTeamMember record);

    int updateByPrimaryKey(SysTeamMember record);
    
    public List<SysTeam> selectAll(SysTeamMember entity);
}