package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.entity.SysTeamMember;

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
}
