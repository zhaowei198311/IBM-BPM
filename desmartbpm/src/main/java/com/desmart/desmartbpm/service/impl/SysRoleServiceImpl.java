package com.desmart.desmartbpm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.SysRoleMapper;
import com.desmart.desmartbpm.entity.SysRole;
import com.desmart.desmartbpm.entity.SysRole;
import com.desmart.desmartbpm.service.SysRoleService;
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
public class SysRoleServiceImpl  implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysRoleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysRole entity) {
		// TODO Auto-generated method stub
		return sysRoleMapper.insert(entity);
	}

	@Override
	public int insertSelective(SysRole entity) {
		// TODO Auto-generated method stub
		return sysRoleMapper.insertSelective(entity);
	}

	@Override
	public SysRole selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysRoleMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysRole entity) {
		// TODO Auto-generated method stub
		return sysRoleMapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateByPrimaryKey(SysRole entity) {
		// TODO Auto-generated method stub
		return sysRoleMapper.updateByPrimaryKey(entity);
	}


	@Override
	public PagedResult<SysRole> queryByPage(SysRole entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysRoleMapper.selectAll(entity));
	}

	@Override
	public List<SysRole> selectAll(SysRole entity) {
		// TODO Auto-generated method stub
		return sysRoleMapper.selectAll(entity);
	}
}
