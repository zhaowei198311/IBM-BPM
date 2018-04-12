package com.desmart.desmartbpm.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.entity.SysRoleUser;
import com.desmart.desmartbpm.service.SysRoleUserService;
import com.desmart.desmartbpm.util.PagedResult;
import com.desmart.desmartbpm.util.UUIDTool;

import java.util.List;

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
	
	
	
	@RequestMapping("/getSysRoleUser")
	@ResponseBody
	public SysRoleUser getSysRoleUser(SysRoleUser sysRoleUser) {
		return sysRoleUserService.selectByPrimaryKey(sysRoleUser.getRoleUid());
	}
	
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
			String[]  roleUser=sysRoleUser.getRoleUid().split(",");
			for (String string : roleUser) {
				sysRoleUser.setMapUid("sysRoleUser:"+UUIDTool.getUUID());
				sysRoleUser.setRoleUid(string);
				sysRoleUser.setMapType(1);
				sysRoleUserService.insert(sysRoleUser);
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
	

}
