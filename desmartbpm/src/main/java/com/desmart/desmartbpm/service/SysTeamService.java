package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.entity.SysTeam;
import com.desmart.desmartbpm.util.PagedResult;

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
}
