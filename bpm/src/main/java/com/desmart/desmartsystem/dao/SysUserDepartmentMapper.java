package com.desmart.desmartsystem.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.desmart.desmartsystem.entity.SysDepartment;
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
	//根据参数查询所有用户下的部门信息
	public List<SysUserDepartment> selectUserDepartmentView(SysUserDepartment sysUserDepartment);
	
	int updateUserDepartment(SysUserDepartment sysUserDepartment);

	public List<SysDepartment> allSysDepartmentMove(@Param("departUid")String departUid, @Param("pageNo")Integer pageNo, 
			@Param("pageSize")Integer pageSize, @Param("condition")String condition);


	/**
	 * 根据员工工号查找对应的部门与组织关联
	 * @param userUids
	 * @return
	 */
	List<SysUserDepartment> listByUserUids(Collection<String> userUids);

}