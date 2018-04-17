package com.desmart.desmartsystem.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.service.SysRoleUserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 **/
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
			SysRoleUser sysRoleUser1=new SysRoleUser();
			sysRoleUser1.setUserUid(sysRoleUser.getUserUid());
			sysRoleUser1.setMapType(sysRoleUser.getMapType());
			sysRoleUserService.delete(sysRoleUser1);
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
			
			SysRoleUser sys = new SysRoleUser();
			sys.setRoleUid(sysRoleUser.getRoleUid());
			
			sysRoleUserService.delete(sys);
			String userUid =sysRoleUser.getUserUid();
			String departUid =sysRoleUser.getDepartUid();
			if(StringUtils.isNotBlank(userUid)) {
			String[]  roleUser=userUid.split(",");
			String[]  departUids=departUid.split(",");
			
			for (int i = 0; i < roleUser.length; i++) {
				sysRoleUser.setUserUid(roleUser[i]);
				sysRoleUser.setDepartUid(departUids[i]);
				sysRoleUserService.insert(sysRoleUser);
			}
			
			
//			List<SysUser> userList=sysRoleUser.getUsers();
//			if(userList!=null && userList.size()>0) {
//				for (SysUser sysUser : userList) {
//					sysRoleUser.setDepartUid(sysUser.getDepartUid());
//					sysRoleUser.setUserUid(sysUser.getUserUid());
//					sysRoleUserService.insert(sysRoleUser);
//				}
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
