package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.util.PagedResult;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-08
 */
public interface SysUserService extends BaseService<SysUser> {
	
	/**
	 * 分页查找用户
	 * @param sysUser
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PagedResult<SysUser> queryByPage(SysUser entity,Integer pageNo,Integer pageSize);
	
	
	/**
	 * 根据条件查找所有用户
	 * @param sysUser
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<SysUser> selectAll(SysUser entity);

	
	/**
	 * 登陆
	 * @param username
	 * @param password
	 * @return
	 */
	List<SysUser> login(String username, String password);

	
	
	/**
	 * 移动端模糊查询全部用户并分页
	 * @param sysUser
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	ServerResponse allSysUserMove(String userUidArrStr, Integer pageNo, Integer pageSize, String condition);
}	
