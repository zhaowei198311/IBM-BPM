package com.desmart.desmartbpm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.SysRoleUserMapper;
import com.desmart.desmartbpm.entity.SysRoleUser;
import com.desmart.desmartbpm.service.SysRoleUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {

	
	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysRoleUser entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.insert(entity);
	}

	@Override
	public int insertSelective(SysRoleUser entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.insertSelective(entity);
	}

	@Override
	public SysRoleUser selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysRoleUser entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateByPrimaryKey(SysRoleUser entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.updateByPrimaryKey(entity);
	}	
	
}
