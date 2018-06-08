package com.desmart.desmartsystem.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
	
}