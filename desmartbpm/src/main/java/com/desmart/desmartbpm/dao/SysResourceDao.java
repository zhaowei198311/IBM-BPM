package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.SysResource;
import com.desmart.desmartbpm.entity.TreeNode;

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