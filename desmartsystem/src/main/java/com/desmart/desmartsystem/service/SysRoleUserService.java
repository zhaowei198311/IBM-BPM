package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysRoleUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public interface SysRoleUserService extends IService<SysRoleUser,String> {
	int delete(SysRoleUser eneity);
	List<SysRoleUser> selectAll(SysRoleUser entity);
}
