package com.desmart.desmartsystem.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.exception.PlatformException;
import com.desmart.desmartsystem.dao.SysRoleMapper;
import com.desmart.desmartsystem.dao.SysRoleResourceMapper;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.entity.SysRole;
import com.desmart.desmartsystem.service.SysRoleService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
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
	
	@Autowired
	private SysRoleResourceMapper sysRoleResourceMapper;
	
	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		sysRoleResourceMapper.deleteByPrimaryKey(id);
		sysRoleUserMapper.deleteByPrimaryKey(id);
		return sysRoleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysRole sysRole) {
		sysRole.setCreateDate(new Date());
		formateStation(sysRole);
		return sysRoleMapper.insert(sysRole);
	}


	@Override
	public int insertSelective(SysRole entity) {
		// TODO Auto-generated method stub
		entity.setCreateDate(new Date());
		return sysRoleMapper.insertSelective(entity);
	}

	@Override
	public SysRole selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return sysRoleMapper.selectByPrimaryKey(id);
	}

	private void formateStation(SysRole sysRole) {
		if (StringUtils.isNotBlank(sysRole.getStation())) {
			String station = sysRole.getStation().trim();
			if (!station.endsWith(";")) {
				station += ";";
			}
			if (!station.matches("([^;]+;)+")) {
				throw new PlatformException("关联岗位的值不符合要求");
			}
			if (station.length() > 80) {
				throw new PlatformException("岗位值过长");
			}
			sysRole.setStation(station);
		}
	}

	@Override
	public int updateByPrimaryKeySelective(SysRole entity) {
		entity.setUpdateDate(new Date());
		formateStation(entity);
		return sysRoleMapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateByPrimaryKey(SysRole entity) {
		// TODO Auto-generated method stub
		entity.setUpdateDate(new Date());
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

	@Override
	public SysRole select(SysRole entity) {
		// TODO Auto-generated method stub
		return sysRoleMapper.select(entity);
	}
}
