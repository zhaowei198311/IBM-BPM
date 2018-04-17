package com.desmart.desmart_systemmanagement.service;

import java.util.List;

import com.desmart.desmart_systemmanagement.entity.SysRole;
import com.desmart.desmart_systemmanagement.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public interface SysRoleService extends IService<SysRole,String> {
	PagedResult<SysRole> queryByPage(SysRole entity,Integer pageNo,Integer pageSize);
	
	
	List<SysRole> selectAll(SysRole entity);
}
