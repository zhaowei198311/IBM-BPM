package com.desmart.desmart_systemmanagement.service;

import java.util.List;

import com.desmart.desmart_systemmanagement.entity.SysRoleResource;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public interface SysRoleResourceService extends IService<SysRoleResource,String> {
	public List<SysRoleResource> selectAll(SysRoleResource sysRoleResource);
}
