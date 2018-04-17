package com.desmart.desmart_systemmanagement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmart_systemmanagement.dao.SysRoleUserMapper;
import com.desmart.desmart_systemmanagement.entity.SysRoleUser;
import com.desmart.desmart_systemmanagement.service.SysRoleUserService;
import com.desmart.desmart_systemmanagement.util.UUIDTool;

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
		entity.setMapUid("sysRoleUser:"+UUIDTool.getUUID());
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

	@Override
	public int delete(SysRoleUser eneity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.delete(eneity);
	}

	@Override
	public List<SysRoleUser> selectAll(SysRoleUser entity) {
		// TODO Auto-generated method stub
		return sysRoleUserMapper.selectAll(entity);
	}	
	
}
