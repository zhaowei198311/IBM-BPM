package com.desmart.desmartsystem.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.desmart.desmartsystem.service.SysUserService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-08
 */
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	SysUserMapper sysUserDao;
	
	@Autowired
	SysUserDepartmentService sysUserDepartmentService;
	
	@Override
	public int insert(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserDao.insert(entity);
	}

	@Override
	public int update(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return sysUserDao.update(entity); 
	} 

	@Override
	public int delete(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		SysUserDepartment userDepartment=new SysUserDepartment();
		userDepartment.setUserUid(entity.getUserUid());
		sysUserDepartmentService.delete(userDepartment);
		
		return sysUserDao.delete(entity);
	}

	@Override 
	public SysUser select(SysUser entity) {
		// TODO Auto-generated method stub
		return sysUserDao.select(entity);
	}

	@Override
	public SysUser findById(SysUser id) {
		// TODO Auto-generated method stub
		return sysUserDao.findById(id);
	}

	@Override
	public PagedResult<SysUser> queryByPage(SysUser entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysUserDao.selectAll(entity));
	}

	@Override
	public List<SysUser> selectAll(SysUser entity) {
		// TODO Auto-generated method stub
		return sysUserDao.selectAll(entity);
	}

	@Override
	public List<SysUser> login(String username, String password) {
		List<SysUser> userList = sysUserDao.login(username, password);
		return userList;
	}

	@Override
	public ServerResponse allSysUserMove(String userUidArrStr, Integer pageNo, Integer pageSize, String condition) {
		PageHelper.startPage(pageNo,pageSize);
		String[] userUidArr = userUidArrStr.split(";");
		if(userUidArr.length==1 && (null==userUidArr[0]||userUidArr[0]=="")) {
			userUidArr = null;
		}
		List<SysUser> userList = sysUserDao.allSysUserMove(userUidArr,condition);
		PageInfo<List<SysUser>> info = new PageInfo(userList);
		return ServerResponse.createBySuccess(info);
	}
}
