package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysTeamMember;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public interface SysTeamMemberService extends IService<SysTeamMember,String> {
	List<SysTeamMember> selectAll(SysTeamMember entity);
	int delete(SysTeamMember entity);
}
