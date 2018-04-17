package com.desmart.desmart_systemmanagement.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmart_systemmanagement.entity.SysResource;
import com.desmart.desmart_systemmanagement.entity.TreeNode;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Repository
public interface SysResourceDao extends BaseMapper<SysResource> {
	List<TreeNode>	resourceTree();	
}