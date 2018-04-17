package com.desmart.desmart_systemmanagement.service;

import java.util.List;

import com.desmart.desmart_systemmanagement.entity.SysDepartment;
import com.desmart.desmart_systemmanagement.entity.TreeNode;
import com.desmart.desmart_systemmanagement.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
public interface SysDepartmentService extends BaseService<SysDepartment> {
	PagedResult<SysDepartment> queryByPage(SysDepartment entity,Integer pageNo,Integer pageSize);
	public List<TreeNode> selectTree(TreeNode entity);
}
