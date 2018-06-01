package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.util.PagedResult;


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
	
	List<SysUser> selectAll(SysUser entity);

	List<SysUser> login(String username, String password);
}	
