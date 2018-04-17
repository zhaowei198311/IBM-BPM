package com.desmart.desmart_systemmanagement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmart_systemmanagement.dao.SysDepartmentDao;
import com.desmart.desmart_systemmanagement.entity.SysDepartment;
import com.desmart.desmart_systemmanagement.entity.TreeNode;
import com.desmart.desmart_systemmanagement.service.SysDepartmentService;
import com.desmart.desmart_systemmanagement.util.BeanUtil;
import com.desmart.desmart_systemmanagement.util.PagedResult;
import com.github.pagehelper.PageHelper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
@Service
public class SysDepartmentServiceImpl   implements SysDepartmentService {

	@Autowired
	public SysDepartmentDao sysDepartmentDao;
	
	@Override
	public int insert(SysDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDepartmentDao.insert(entity);
	}

	@Override
	public int update(SysDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDepartmentDao.update(entity);
	}

	@Override
	public int delete(SysDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDepartmentDao.delete(entity);
	}

	@Override
	public SysDepartment select(SysDepartment entity) {
		// TODO Auto-generated method stub
		return sysDepartmentDao.select(entity);
	}

	@Override
	public SysDepartment findById(SysDepartment id) {
		// TODO Auto-generated method stub
		return sysDepartmentDao.findById(id);
	}

	@Override
	public PagedResult<SysDepartment> queryByPage(SysDepartment entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysDepartmentDao.selectAll(entity));
	}

	@Override
	public List<TreeNode> selectTree(TreeNode entity) {
		// TODO Auto-generated method stub
		return sysDepartmentDao.selectTree(entity);
	}
	
}
