package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysTeamMember;

@Repository
public interface SysTeamMemberMapper {
    int deleteByPrimaryKey(String teamUid);

    int insert(SysTeamMember record);

    int insertSelective(SysTeamMember record);

    SysTeamMember selectByPrimaryKey(String memberUid);

    int updateByPrimaryKeySelective(SysTeamMember record);

    int updateByPrimaryKey(SysTeamMember record);
    
    public List<SysTeamMember> selectAll(SysTeamMember entity);
    
    int delete(SysTeamMember entity);
    
    List<SysTeamMember> selectTeamUser(SysTeamMember entity);
}