package com.desmart.desmart_systemmanagement.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmart_systemmanagement.dao.SysTeamMapper;
import com.desmart.desmart_systemmanagement.dao.SysTeamMemberMapper;
import com.desmart.desmart_systemmanagement.entity.SysTeam;
import com.desmart.desmart_systemmanagement.service.SysTeamService;
import com.desmart.desmart_systemmanagement.util.BeanUtil;
import com.desmart.desmart_systemmanagement.util.PagedResult;
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
public class SysTeamServiceImpl implements SysTeamService {
	@Autowired
	private SysTeamMapper sysTeamMapper;
	@Autowired
	private SysTeamMemberMapper sysTeamMemberMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		sysTeamMemberMapper.deleteByPrimaryKey(id);
		return sysTeamMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysTeam entity) {
		// TODO Auto-generated method stub
			entity.getIsClosed();
		Date date=new Date();
		if(entity.getIsClosed().intValue()==0) {
			entity.setCloseDate(date);
		}
		entity.setCreateDate(date);
		entity.setUpdateDate(date);
		return sysTeamMapper.insert(entity);
	}

	@Override
	public int insertSelective(SysTeam entity) {
		// TODO Auto-generated method stub
		return sysTeamMapper.insertSelective(entity);
	}

	@Override
	public SysTeam selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysTeamMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysTeam entity) {
		// TODO Auto-generated method stub
		Date date=new Date();
		if(entity.getIsClosed().intValue()==0) {
			entity.setCloseDate(date);
		}
		entity.setCreateDate(date);
		entity.setUpdateDate(date);
		return sysTeamMapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateByPrimaryKey(SysTeam entity) {
		// TODO Auto-generated method stub
		return sysTeamMapper.updateByPrimaryKey(entity);
	}	
	

	@Override
	public PagedResult<SysTeam> queryByPage(SysTeam entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysTeamMapper.selectAll(entity));
	}

	@Override
	public List<SysTeam> selectAll(SysTeam entity) {
		// TODO Auto-generated method stub
		return sysTeamMapper.selectAll(entity);
	}

	@Override
	public SysTeam selectByPrimary(SysTeam sysTeam) {
		// TODO Auto-generated method stub
		return sysTeamMapper.selectByPrimary(sysTeam);
	}
}
