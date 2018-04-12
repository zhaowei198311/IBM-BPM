package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.SysTeamMapper;
import com.desmart.desmartbpm.entity.SysTeam;
import com.desmart.desmartbpm.entity.SysTeam;
import com.desmart.desmartbpm.service.SysTeamService;
import com.desmart.desmartbpm.util.BeanUtil;
import com.desmart.desmartbpm.util.PagedResult;
import com.github.pagehelper.PageHelper;

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
public class SysTeamServiceImpl implements SysTeamService {
	@Autowired
	private SysTeamMapper sysTeamMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysTeamMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysTeam entity) {
		// TODO Auto-generated method stub
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
}
