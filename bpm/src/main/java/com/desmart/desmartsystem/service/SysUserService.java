package com.desmart.desmartsystem.service;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
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
	 * PC端模糊查询用户
	 * @param condition
	 * @return
	 */
	ServerResponse querySysUserByConditionPC(String condition);
	
	/**
	 * 移动端模糊查询全部用户并分页
	 * @param sysUser
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	ServerResponse allSysUserMove(String userUidArrStr, Integer pageNo, Integer pageSize, String condition);


	/**
	 * 根据字段来选择
	 * @param mergedFormData  表单内容
	 * @param field  指定字段  如user1;user2;user3;
	 * @return
	 */
	List<SysUser> searchByField(JSONObject mergedFormData, String field);

	/**
	 * 根据角色组uid集合查询用户数据
	 * @param teamUidList
	 * @return
	 */
	List<SysUser> searchByTeam(List<String> teamUidList);

	/**
	 * 根据角色组uid集合和部门编号查询
	 * @param teamUidList
	 * @param departNo
	 * @return
	 */
	List<SysUser> searchbyTeamAndDepartment(List<String> teamUidList, String departNo);

	/**
	 * 根据角色组uid集合和公司编码查询
	 * @param objIdList
	 * @param companyNum
	 * @return
	 */
	List<SysUser> searchByTeamAndCompany(List<String> objIdList, String companyNum);

	/**
	 * 根据角色id集合查询角色用户数据
	 * @param roleUidList 角色id集合
	 * @return
	 */
	List<SysUser> searchByRoleUidList(List<String> roleUidList);


	/**
	 * 根据角色id集合加部门uid查询
	 * @param roleUidList
	 * @param departNo
	 * @return
	 */
	List<SysUser> searchByRoleUidListAndDepartment(List<String> roleUidList, String departNo);

	/**
	 * 根据角色id集合加公司编码查询
	 * @param roleUidList
	 * @param companyNum
	 * @return
	 */
	List<SysUser> searchByRoleUidListAndCompany(List<String> roleUidList, String companyNum);

	/**
	 * 根据指定员工号列表，获得用户列表
	 * @param userUids  指定的员工号列表
	 * @return 不含重复数据
	 */
	List<SysUser> searchByUserUids(Collection<String> userUids);

	/**
	 * 根据用户查询上级用户
	 * @param sysUser
	 */
	List<SysUser> searchByLeaderOfPreActivityUser(SysUser sysUser);
}	
