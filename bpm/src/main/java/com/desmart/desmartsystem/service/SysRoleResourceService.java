package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysRoleResource;

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
