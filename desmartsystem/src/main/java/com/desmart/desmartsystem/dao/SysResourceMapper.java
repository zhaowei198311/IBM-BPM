package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysResource;
import com.desmart.desmartsystem.entity.TreeNode;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Repository
public interface SysResourceMapper extends BaseMapper<SysResource> {
	List<TreeNode>	resourceTree();	
}