package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysTeam;
import com.desmart.desmartsystem.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public interface SysTeamService extends IService<SysTeam,String> {
	PagedResult<SysTeam> queryByPage(SysTeam entity,Integer pageNo,Integer pageSize);
	
	List<SysTeam> selectAll(SysTeam entity);
	
	public SysTeam selectByPrimary(SysTeam sysTeam);
}
