package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysUserDepartment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-27
 */
public interface SysUserDepartmentService extends BaseService<SysUserDepartment> {
	List<SysUserDepartment> selectAll(SysUserDepartment sysUserDepartment);
	public List<SysUserDepartment> selectUserDepartmentView(SysUserDepartment sysUserDepartment);
}
