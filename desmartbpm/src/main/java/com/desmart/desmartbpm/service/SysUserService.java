package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.entity.SysUser;
import com.desmart.desmartbpm.util.PagedResult;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-08
 */
public interface SysUserService extends BaseService<SysUser> {
	PagedResult<SysUser> queryByPage(SysUser entity,Integer pageNo,Integer pageSize);
}	
