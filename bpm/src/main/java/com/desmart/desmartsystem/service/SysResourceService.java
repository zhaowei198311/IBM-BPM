package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysResource;
import com.desmart.desmartsystem.entity.TreeNode;
import com.desmart.desmartsystem.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public interface SysResourceService extends BaseService<SysResource> {
	PagedResult<SysResource> queryByPage(SysResource entity,Integer pageNo,Integer pageSize);
	List<TreeNode>	resourceTree();
	List<SysResource> selectResourceByUserId(String userId);
}
