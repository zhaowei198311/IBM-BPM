package com.desmart.desmartsystem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.dao.SysUserDepartmentMapper;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-27
 */
@Service
public class SysUserDepartmentServiceImpl  implements SysUserDepartmentService {

	 
	@Autowired
	private SysUserDepartmentMapper sysUserDepartmentDao;

	@Override 
	public int insert(SysUserDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserDepartmentDao.insert(entity);
	}

	@Override
	public int update(SysUserDepartment entity) throws Exception { 
		// TODO Auto-generated method stub
		return sysUserDepartmentDao.update(entity);
	}

	@Override
	public int delete(SysUserDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserDepartmentDao.delete(entity);
	}

	@Override
	public SysUserDepartment select(SysUserDepartment entity) {
		// TODO Auto-generated method stub
		return sysUserDepartmentDao.select(entity);
	}

	@Override
	public SysUserDepartment findById(SysUserDepartment id) {
		// TODO Auto-generated method stub
		return sysUserDepartmentDao.findById(id);
	}

	@Override
	public List<SysUserDepartment> selectAll(SysUserDepartment sysUserDepartment) {
		// TODO Auto-generated method stub
		return sysUserDepartmentDao.selectAll(sysUserDepartment);
	}

	@Override
	public List<SysUserDepartment> selectUserDepartmentView(SysUserDepartment sysUserDepartment) {
		// TODO Auto-generated method stub
		return sysUserDepartmentDao.selectUserDepartmentView(sysUserDepartment);
	}

	@Override
	public ServerResponse allSysDepartmentMove(String departUid, Integer pageNo,
			Integer pageSize,String condition) {
		PageHelper.startPage(pageNo,pageSize);
		List<SysDepartment> departList = sysUserDepartmentDao.allSysDepartmentMove(departUid,pageNo,pageSize,condition);
		PageInfo<List<SysDepartment>> info = new PageInfo(departList);
		return ServerResponse.createBySuccess(info);
	}
	
	
}
