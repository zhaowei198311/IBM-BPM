package com.desmart.desmart_systemmanagement.service;

import java.util.List;

import com.desmart.desmart_systemmanagement.entity.SysTeamMember;

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
