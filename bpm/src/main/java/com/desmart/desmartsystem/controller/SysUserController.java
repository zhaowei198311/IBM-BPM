package com.desmart.desmartsystem.controller;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeam;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.SysRoleUserService;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.desmart.desmartsystem.service.SysUserService;
import com.desmart.desmartsystem.util.AssignPersonnel;
import com.desmart.desmartsystem.util.PagedResult;
import com.desmart.desmartsystem.util.UUIDTool;

/**
 * <p>
 *  鍓嶇鎺у埗鍣�
 * </p>
 *
 * @author xsf
 * @since 2018-04-08
 */
@Controller
@RequestMapping("/sysUser")
public class SysUserController {
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysUserDepartmentService sysUserDepartmentService;
	
	@RequestMapping(value="/index")
	public String drafts(){
		return "desmartsystem/usermanagement/index";
	}
	
	
	/**
	 * 全局管理配置页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(value="/globalConfig")
	public String globalConfig(){
		return "desmartsystem/usermanagement/globalConfig";
	}
	
	@RequestMapping(value="/test")
	public String test(){
		return "desmartsystem/test";
	}
	
	/**
	 * 用户选择角色
	 * @param userUid
	 * @return
	 */
	@RequestMapping(value="/userChooseRole")
	public ModelAndView userChooseRole(String userUid){
		ModelAndView mode=new ModelAndView("desmartsystem/usermanagement/common/userChooseRole");
		mode.addObject("userUid", userUid);
		return mode;
	}
	
	
	/**
	 * 环节配置选人
	 * @param AssignPersonnel
	 * @return
	 */
	@RequestMapping(value="/assign_personnel")
	public ModelAndView assign_personnel(AssignPersonnel assignPersonnel){
		ModelAndView model = new ModelAndView("desmartsystem/usermanagement/assign_personnel");
		model.addObject("assignPersonnel", JSONObject.toJSON(assignPersonnel));
		return model;
	}

	/**
	 * 角色 用户 角色组 选人
	 * @param String id 
	 * * @param String isSingle
	 * @return
	 */
	@RequestMapping(value="/select_personnel")
	public ModelAndView select_personnel(String id, String isSingle){
		ModelAndView model = new ModelAndView("desmartsystem/usermanagement/select_personnel");
		model.addObject("id",id);
		model.addObject("isSingle",isSingle);
		return model;
	}
	
	/**
	 * 组织管理页面跳转
	 */
	@RequestMapping(value="/organization")
	public String organization(){
		return "desmartsystem/usermanagement/organization";
	}
	
	
	/**
	 * 用户管理页面跳转
	 */
	@RequestMapping(value="/user")
	public String user(){
		return "desmartsystem/usermanagement/user";
	}
	
	
	/**
	 * 根据条件分页查询所有用户
	 */
	@RequestMapping(value="/allSysUser")
	@ResponseBody
	public PagedResult<SysUser> allSysUser(SysUser sysUser,Integer pageNo,Integer pageSize){
		PagedResult<SysUser> queryByPage=sysUserService.queryByPage(sysUser,pageNo,pageSize);
		List<SysUser> sysUsersList=new ArrayList<SysUser>();
		List<SysUser> sysUsers=queryByPage.getDataList();
		for (SysUser sysUser1 : sysUsers) {
			SysUserDepartment sUserDepartment=new SysUserDepartment();
			sUserDepartment.setUserUid(sysUser1.getUserUid());
			sysUser1.setSysUserDepartmentList(sysUserDepartmentService.selectUserDepartmentView(sUserDepartment));
			sysUsersList.add(sysUser1);
		}
		queryByPage.setDataList(sysUsersList);
		return queryByPage; 
	}
	
	/**
	 * 移动端模糊查询全部用户并分页
	 * @param sysUser
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/allSysUserMove")
	@ResponseBody
	public ServerResponse allSysUserMove(String userUidArrStr,Integer pageNo,Integer pageSize,String condition){
		try {
			return sysUserService.allSysUserMove(userUidArrStr,pageNo,pageSize,condition);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
	
	
	/**
	 * 根据条件查询用户列表
	 */
	@RequestMapping(value="/userList")
	@ResponseBody
	public List<SysUser> userList(SysUser sysUser){
		return sysUserService.selectAll(sysUser);
	}
	
	
	/**
	 * 根据,号用户id 返回用户集合
	 */
	@RequestMapping(value="/userByIds")
	@ResponseBody 
	public List<SysUser> userByIds(String userIds){
		List<SysUser> userList=new ArrayList<SysUser>();
		if(StringUtils.isNotBlank(userIds)) {
			String users[]=userIds.split(";");
			for (String string : users) {
				SysUser user=new SysUser();
				user.setUserUid(string);
				userList.add(sysUserService.select(user));
			}
		}
		return userList;
	}
	
	/**
	 * 根据用户id返回用户信息
	 */
	@RequestMapping(value = "/userDetail")
    public ModelAndView userDetail(SysUser sysUser) {
		ModelAndView moAndView=new ModelAndView("desmartsystem/usermanagement/edit_user");
		moAndView.addObject("sysUser",sysUserService.findById(sysUser));
        return moAndView;
    }
 	
	
	/**
	 * 根据用户id返回用户信息
	 */
	@RequestMapping("/getSysUser")
	@ResponseBody
	public SysUser getSysUser(SysUser sysUser) {
		return sysUserService.findById(sysUser);
	}
	
	/**
	 * 修改用户信息
	 */
	@RequestMapping("/updateSysUser")
	@ResponseBody
	public String updateSysUser(SysUser sysUser) {
		try {	
			sysUserService.update(sysUser);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	
	/**
	 * 添加用户信息
	 */
	@RequestMapping("/addSysUser")
	@ResponseBody
	public String addSysUser(SysUser sysUser) {
		try {	
			sysUserService.insert(sysUser);
			String departUid=sysUser.getDepartUid();
			if(departUid!=null) {
				SysUserDepartment sysUserDepartment=new SysUserDepartment();
				sysUserDepartment.setUduid("sysUserDepartment:"+UUIDTool.getUUID());
				sysUserDepartment.setUserUid(sysUser.getUserUid());
				sysUserDepartment.setDepartUid(departUid);
				sysUserDepartmentService.insert(sysUserDepartment);
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	
	/**
	 * 根据用户userUid判断当前用户名是否重复
	 */
	@ResponseBody  
	@RequestMapping(value="userexists")
	public boolean userexists(SysUser cpUser) {
		if(sysUserService.select(cpUser)!=null){
			return false;  
		}
		return true;  
	}
	
	/**
	 * 删除用户信息
	 */
	@RequestMapping("/deleteSysUser")
	@ResponseBody
	public String deleteSysUser(SysUser sysUser) {
		try {	
			sysUserService.delete(sysUser);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
