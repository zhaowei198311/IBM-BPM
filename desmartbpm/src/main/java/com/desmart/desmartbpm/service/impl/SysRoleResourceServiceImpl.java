package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.SysRoleResourceMapper;
import com.desmart.desmartbpm.entity.SysRoleResource;
import com.desmart.desmartbpm.service.SysRoleResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Service
public class SysRoleResourceServiceImpl  implements SysRoleResourceService {

	@Autowired
	private SysRoleResourceMapper sysRoleResourceMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysRoleResourceMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysRoleResource entity) {
		// TODO Auto-generated method stub
		return sysRoleResourceMapper.insert(entity);
	}

	@Override
	public int insertSelective(SysRoleResource entity) {
		// TODO Auto-generated method stub
		return sysRoleResourceMapper.insertSelective(entity);
	}

	@Override
	public SysRoleResource selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysRoleResourceMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysRoleResource entity) {
		// TODO Auto-generated method stub
		return sysRoleResourceMapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateByPrimaryKey(SysRoleResource entity) {
		// TODO Auto-generated method stub
		return sysRoleResourceMapper.updateByPrimaryKey(entity);
	}
	
}
