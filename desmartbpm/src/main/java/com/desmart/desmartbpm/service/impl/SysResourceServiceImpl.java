package com.desmart.desmartbpm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.SysResourceDao;
import com.desmart.desmartbpm.entity.SysResource;
import com.desmart.desmartbpm.entity.TreeNode;
import com.desmart.desmartbpm.service.SysResourceService;
import com.desmart.desmartbpm.util.BeanUtil;
import com.desmart.desmartbpm.util.PagedResult;
import com.github.pagehelper.PageHelper;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {
	
	@Autowired
	SysResourceDao sysResourceDao;
	
	@Override
	public int insert(SysResource entity) throws Exception {
		// TODO Auto-generated method stub
		return sysResourceDao.insert(entity);
	}

	@Override
	public int update(SysResource entity) throws Exception {
		// TODO Auto-generated method stub
		return sysResourceDao.update(entity);
	}

	@Override
	public int delete(SysResource entity) throws Exception {
		// TODO Auto-generated method stub
		return sysResourceDao.delete(entity);
	}

	@Override
	public SysResource select(SysResource entity) {
		// TODO Auto-generated method stub
		return sysResourceDao.select(entity);
	}

	@Override
	public SysResource findById(SysResource id) {
		// TODO Auto-generated method stub
		return sysResourceDao.findById(id);
	}

	@Override
	public PagedResult<SysResource> queryByPage(SysResource entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysResourceDao.selectAll(entity));
	}

	@Override
	public List<TreeNode> resourceTree() {
		// TODO Auto-generated method stub
		return sysResourceDao.resourceTree();
	}
	
}
