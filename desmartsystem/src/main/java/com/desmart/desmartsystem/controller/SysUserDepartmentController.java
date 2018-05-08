package com.desmart.desmartsystem.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.desmart.desmartsystem.util.UUIDTool;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-04-27
 */
@Controller
@RequestMapping("/sysUserDepartment")
public class SysUserDepartmentController {
	@Autowired
	private SysUserDepartmentService sysUserDepartmentService;
	
	@RequestMapping(value="/selectAll") 
	@ResponseBody
	public List<SysUserDepartment> selectAll(SysUserDepartment sysUserDepartment){
		return sysUserDepartmentService.selectAll(sysUserDepartment); 
	}
	 
	
	@RequestMapping("/addSysUserDepartments")
	@ResponseBody
	public String addSysUserDepartments(SysUserDepartment sysUserDepartment) {
		try {	
			String departuid=sysUserDepartment.getDepartUid();
			String isManager=sysUserDepartment.getIsManager();
			if(StringUtils.isNotBlank(departuid)) {
				String[]  departuids=departuid.split(",");
				String[]  isManagers=isManager.split(",");
				for (int i = 0; i < departuids.length; i++) {
					sysUserDepartment.setUduid("sysUserDepartment:"+UUIDTool.getUUID());
					sysUserDepartment.setDepartUid(departuids[i]);
					sysUserDepartment.setIsManager(isManagers[i]);
					sysUserDepartmentService.insert(sysUserDepartment);
				}
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysUserDepartment")
	@ResponseBody
	public String deleteSysUserDepartment(SysUserDepartment sysUserDepartment) {
		try {	
			sysUserDepartmentService.delete(sysUserDepartment);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
