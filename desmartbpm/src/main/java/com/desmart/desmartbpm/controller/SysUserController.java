package com.desmart.desmartbpm.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.entity.SysUser;
import com.desmart.desmartbpm.service.SysUserService;
import com.desmart.desmartbpm.util.PagedResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
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
	
	@RequestMapping(value="/index")
	public String drafts(){
		return "usermanagement/index";
	}
	
	@RequestMapping(value="/organization")
	public String organization(){
		return "usermanagement/organization";
	}
	
	@RequestMapping(value="/user")
	public String user(){
		return "usermanagement/user";
	}
	
	
	@RequestMapping(value="/allSysUser")
	@ResponseBody
	public PagedResult<SysUser> allSysUser(SysUser sysUser,Integer pageNo,Integer pageSize){
		PagedResult<SysUser> queryByPage=sysUserService.queryByPage(sysUser,pageNo,pageSize);
		return queryByPage; 
	}
	
	
	
	
	@RequestMapping("/getSysUser")
	@ResponseBody
	public SysUser getSysUser(SysUser sysUser) {
		return sysUserService.findById(sysUser);
	}
	
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
