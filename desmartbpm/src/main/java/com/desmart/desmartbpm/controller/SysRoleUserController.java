package com.desmart.desmartbpm.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.entity.SysRoleUser;
import com.desmart.desmartbpm.entity.SysUser;
import com.desmart.desmartbpm.service.SysRoleUserService;
import com.desmart.desmartbpm.util.PagedResult;
import com.desmart.desmartbpm.util.UUIDTool;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Controller
@RequestMapping("/sysRoleUser")
public class SysRoleUserController {

	
	@Autowired
	private SysRoleUserService sysRoleUserService;
	
	
	
	
	@RequestMapping("/updateSysRoleUser")
	@ResponseBody
	public String updateSysRoleUser(SysRoleUser sysRoleUser) {
		try {	
			sysRoleUserService.updateByPrimaryKeySelective(sysRoleUser);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/addSysRoleUser")
	@ResponseBody
	public String addSysRoleUser(SysRoleUser sysRoleUser) {
		try {	
			sysRoleUserService.delete(sysRoleUser);
			String roleUid =sysRoleUser.getRoleUid();
			if(StringUtils.isNotBlank(roleUid)) {
				String[]  roleUser=roleUid.split(",");
				for (String string : roleUser) {
					sysRoleUser.setMapUid("sysRoleUser:"+UUIDTool.getUUID());
					sysRoleUser.setRoleUid(string);
					sysRoleUserService.insert(sysRoleUser);
				}
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	

	@RequestMapping("/addRoleUser")
	@ResponseBody
	public String addRoleUser(SysRoleUser sysRoleUser) {
		try {	
			sysRoleUserService.delete(sysRoleUser);
			String roleUid =sysRoleUser.getRoleUid();
			if(StringUtils.isNotBlank(roleUid)) {
				String[]  roleUser=roleUid.split(",");
				for (String string : roleUser) {
					sysRoleUser.setRoleUid(string);
					sysRoleUserService.insert(sysRoleUser);
				}
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/insertRoleUser")
	@ResponseBody
	public String insertRoleUser(SysRoleUser sysRoleUser) {
		try {	
			sysRoleUserService.delete(sysRoleUser);
			
			List<SysUser> userList=sysRoleUser.getUsers();
			if(userList!=null && userList.size()>0) {
				for (SysUser sysUser : userList) {
					sysRoleUser.setDepartUid(sysUser.getDepartUid());
					sysRoleUser.setUserUid(sysUser.getUserUid());
					sysRoleUserService.insert(sysRoleUser);
				}
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysRoleUser")
	@ResponseBody
	public String deleteSysRoleUser(String roleUid) {
		try {	
			sysRoleUserService.deleteByPrimaryKey(roleUid);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping(value="/allSysRoleUser")
	@ResponseBody
	public List<SysRoleUser> allSysRoleUser(SysRoleUser sysRoleUser){
		return sysRoleUserService.selectAll(sysRoleUser); 
	}

	@RequestMapping("/getSysRoleUser")
	@ResponseBody
	public SysRoleUser getSysRoleUser(SysRoleUser sysRoleUser) {
		SysRoleUser sysRoleUser1=sysRoleUserService.selectByPrimaryKey(sysRoleUser.getRoleUid());
		if(sysRoleUser1==null) {
			return sysRoleUser;
		}
		return sysRoleUser1;
	}
	
}
