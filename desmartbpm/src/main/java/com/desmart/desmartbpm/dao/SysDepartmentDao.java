package com.desmart.desmartbpm.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.SysDepartment;
import com.desmart.desmartbpm.entity.TreeNode;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
@Repository
public interface SysDepartmentDao extends BaseMapper<SysDepartment> {
	public List<TreeNode> selectTree(TreeNode entity);
}