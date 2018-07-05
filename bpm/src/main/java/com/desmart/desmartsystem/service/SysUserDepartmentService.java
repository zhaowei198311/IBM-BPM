package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.util.PagedResult;

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
	/**
	 * 移动端带分页查询除某个部门外的部门列表
	 * @param sysDepartment
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	ServerResponse allSysDepartmentMove(String departUid, Integer pageNo,
				Integer pageSize,String condition);
}
