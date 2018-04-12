package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.SysPositionMapper;
import com.desmart.desmartbpm.entity.SysPosition;
import com.desmart.desmartbpm.service.SysPositionService;

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
public class SysPositionServiceImpl implements SysPositionService {
	
	@Autowired
	private SysPositionMapper sysRoleUserMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(SysPosition entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.insert(entity);
	}

	@Override
	public int insertSelective(SysPosition entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.insertSelective(entity);
	}

	@Override
	public SysPosition selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(SysPosition entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(SysPosition entity) {
		// TODO Auto-generated method stub
		return 0;
	}
}
