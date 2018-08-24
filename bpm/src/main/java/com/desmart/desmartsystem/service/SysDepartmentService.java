package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.JSTreeNode;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.TreeNode;
import com.desmart.desmartsystem.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
public interface SysDepartmentService extends BaseService<SysDepartment> {
	
	/**
     * 查询所有部门
     * @param queryByPage
     * @param SysDepartment
     */
	PagedResult<SysDepartment> queryByPage(SysDepartment entity,Integer pageNo,Integer pageSize);
	
	
	
	/**
     * 部门树形菜单
     * @param queryByPage
     * @param SysDepartment
     */
	public List<TreeNode> selectTree(TreeNode entity);
	
	/**
	 * 根据传入父级部门的id查询父级部门信息以及子级部门
	 * @param department 
	 * @return
	 */
	public List<JSTreeNode> selectDepartmentTreeNodeByParent(String id);
	
	/**
     * 根据
     * @param queryByPage
     * @param SysDepartment
     */
	ServerResponse queryDepartByNoAndName(SysDepartment sysDepartment);
	
	/**
     * 根据departUid
     * @param queryDepartParentsByDepartId
     * @param SysDepartment
     */
	ServerResponse<List<SysDepartment>> queryDepartParentsByDepartId(String departUid);
}
