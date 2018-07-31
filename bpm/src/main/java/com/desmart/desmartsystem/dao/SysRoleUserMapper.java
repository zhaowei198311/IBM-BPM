package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysRoleUser;

@Repository
public interface SysRoleUserMapper {
    int deleteByPrimaryKey(String mapUid);
    
    int delete(SysRoleUser eneity); 

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(String mapUid);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);
    
    
    List<SysRoleUser> selectAll(SysRoleUser entity);
    
    //查询角色下的用户用;号分割
    List<SysRoleUser> selectByRoleUser(SysRoleUser entity);
    
    /**
     * 根据角色uid查询所有映射集合
     * @param roleUid
     * @return
     */
    List<SysRoleUser> selectByRoleUid(String roleUid);
}