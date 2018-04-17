package com.desmart.desmart_systemmanagement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmart_systemmanagement.dao.SysTeamMemberMapper;
import com.desmart.desmart_systemmanagement.entity.SysTeamMember;
import com.desmart.desmart_systemmanagement.service.SysTeamMemberService;
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
public class SysTeamMemberServiceImpl implements SysTeamMemberService {
	@Autowired
	private SysTeamMemberMapper sysTeamMemberMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysTeamMemberMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysTeamMember entity) {
		// TODO Auto-generated method stub
		entity.setMemberUid(UUIDTool.getUUID());
		return sysTeamMemberMapper.insert(entity);
	}

	@Override
	public int insertSelective(SysTeamMember entity) {
		// TODO Auto-generated method stub
		return sysTeamMemberMapper.insertSelective(entity);
	}

	@Override
	public SysTeamMember selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysTeamMemberMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysTeamMember entity) {
		// TODO Auto-generated method stub
		return sysTeamMemberMapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateByPrimaryKey(SysTeamMember entity) {
		// TODO Auto-generated method stub
		return sysTeamMemberMapper.updateByPrimaryKey(entity);
	}

	@Override
	public List<SysTeamMember> selectAll(SysTeamMember entity) {
		// TODO Auto-generated method stub
		return sysTeamMemberMapper.selectAll(entity);
	}	
}
