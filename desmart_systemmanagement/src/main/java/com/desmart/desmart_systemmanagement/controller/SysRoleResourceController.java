package com.desmart.desmart_systemmanagement.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmart_systemmanagement.entity.SysRoleResource;
import com.desmart.desmart_systemmanagement.service.SysRoleResourceService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Controller
@RequestMapping("/sysRoleResource")
public class SysRoleResourceController {
	
	
	@Autowired
	private SysRoleResourceService sysRoleResourceService;
	
	@RequestMapping(value="/allSysRoleResource")
	@ResponseBody
	public List<SysRoleResource> allSysRoleResource(SysRoleResource sysRoleResource){
		return sysRoleResourceService.selectAll(sysRoleResource);
	}
	
	
	@RequestMapping("/adSysRoleResource")
	@ResponseBody
	public String adSysRoleResource(SysRoleResource sysRoleResource) {
		try {	
			sysRoleResourceService.deleteByPrimaryKey(sysRoleResource.getRoleUid());
			String resourceUid=sysRoleResource.getResourceUid();
			if(StringUtils.isNotBlank(resourceUid)) {
				String[]  roleUser=resourceUid.split(",");
				for (String string : roleUser) {
					sysRoleResource.setResourceUid(string);
					sysRoleResourceService.insert(sysRoleResource);
				}
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	
}
