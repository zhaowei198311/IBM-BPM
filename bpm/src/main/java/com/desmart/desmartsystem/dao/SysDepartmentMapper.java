package com.desmart.desmartsystem.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.TreeNode;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
@Repository
public interface SysDepartmentMapper extends BaseMapper<SysDepartment> {
	
	//部门树形节点
	public List<TreeNode> selectTree(TreeNode entity);
	
	//批量添加所有部门信息
	int inserBatch(List<SysDepartment> list);
	
	//查询所有部门信息
	public List<SysDepartment> selectDesmarts();
	
	//根据部门编码查询部门
	SysDepartment getSysDepartmentByDepartNo(@Param("departNo")String departNo);

	//根据部门编码和部门名称查询部门
	public SysDepartment queryDepartByNoAndName(SysDepartment sysDepartment);

	//根据部门id查询其父部门id
	public SysDepartment queryParentDepartByDepartId(@Param("departUid")String departUid);

	SysDepartment queryByPrimaryKey(String departUid);

	/**
	 * 根据条件查询部门的父级树列表
	 * @param sysDepartment
	 * @return
	 */
	public List<SysDepartment> queryByConditionToParentTree(SysDepartment sysDepartment);
	
}