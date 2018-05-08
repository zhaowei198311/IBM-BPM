package com.desmart.desmartsystem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.SysUserService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-08
 */
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	SysUserMapper sysUserDao;
	
	@Override
	public int insert(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserDao.insert(entity);
	}

	@Override
	public int update(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserDao.update(entity);
	} 

	@Override
	public int delete(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserDao.delete(entity);
	}

	@Override 
	public SysUser select(SysUser entity) {
		// TODO Auto-generated method stub
		return sysUserDao.select(entity);
	}

	@Override
	public SysUser findById(SysUser id) {
		// TODO Auto-generated method stub
		return sysUserDao.findById(id);
	}

	@Override
	public PagedResult<SysUser> queryByPage(SysUser entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysUserDao.selectAll(entity));
	}

	@Override
	public List<SysUser> selectAll(SysUser entity) {
		// TODO Auto-generated method stub
		return sysUserDao.selectAll(entity);
	}
	
}
