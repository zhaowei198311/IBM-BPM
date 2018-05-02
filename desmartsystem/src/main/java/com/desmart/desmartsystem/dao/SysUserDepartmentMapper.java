package com.desmart.desmartsystem.dao;

import java.util.List;

import com.desmart.desmartsystem.entity.SysUserDepartment;

/**
 * <p>
  *  Mapper 接口
 * </p>
 * 
 * @author xsf
 * @since 2018-04-27
 */
public interface SysUserDepartmentMapper extends BaseMapper<SysUserDepartment> {
	public List<SysUserDepartment> selectUserDepartmentView(SysUserDepartment sysUserDepartment);
}