package com.desmart.desmart_systemmanagement.service;

import java.util.List;

import com.desmart.desmart_systemmanagement.entity.SysResource;
import com.desmart.desmart_systemmanagement.entity.TreeNode;
import com.desmart.desmart_systemmanagement.util.PagedResult;

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
}
