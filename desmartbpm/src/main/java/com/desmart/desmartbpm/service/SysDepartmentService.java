package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.entity.SysDepartment;
import com.desmart.desmartbpm.entity.TreeNode;
import com.desmart.desmartbpm.util.PagedResult;

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
