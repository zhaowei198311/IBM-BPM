package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.SysPositionUserMapper;
import com.desmart.desmartbpm.entity.SysPositionUser;
import com.desmart.desmartbpm.service.SysPositionUserService;

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
public class SysPositionUserServiceImpl implements SysPositionUserService {
	@Autowired
	private SysPositionUserMapper sysRoleUserMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(SysPositionUser entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.insert(entity);
	}

	@Override
	public int insertSelective(SysPositionUser entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.insertSelective(entity);
	}

	@Override
	public SysPositionUser selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(SysPositionUser entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(SysPositionUser entity) {
		// TODO Auto-generated method stub
		return 0;
	}
}
