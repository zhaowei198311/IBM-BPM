package com.desmart.desmartsystem.dao;


import java.util.List;

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
	public List<TreeNode> selectTree(TreeNode entity);
}