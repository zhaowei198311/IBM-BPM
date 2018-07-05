package com.desmart.desmartsystem.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartsystem.entity.SysRole;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.SysRoleService;
import com.desmart.desmartsystem.util.PagedResult;
import com.desmart.desmartsystem.util.UUIDTool;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Controller
@RequestMapping("/sysRole")
public class SysRoleController {
	
	@Autowired
	private SysRoleService sysRoleService;
	
	
	@RequestMapping(value="/system_role")
	public String system_role(){
		return "desmartsystem/usermanagement/system_role";
	}
	
	@RequestMapping(value="/role")
	public String role(){
		return "desmartsystem/usermanagement/role";
	}
	
	//绑定业务角色
	@RequestMapping(value="/bindingRole")
	public ModelAndView bindingRole(String userUid){
		ModelAndView ml =new ModelAndView("desmartsystem/usermanagement/bindingRole");
		ml.addObject("userUid", userUid);
		return ml;
	}
	
	
	@RequestMapping(value="/allSysRole")
	@ResponseBody
	public PagedResult<SysRole> allSysRole(SysRole sysRole,Integer pageNo,Integer pageSize){
		PagedResult<SysRole> queryByPage=sysRoleService.queryByPage(sysRole,pageNo,pageSize);
		return queryByPage; 
	}
	
	
	@RequestMapping(value="/roleList")
	@ResponseBody
	public List<SysRole> roleList(SysRole sysRole){
		return sysRoleService.selectAll(sysRole); 
	}
	
	
	
	@RequestMapping("/getSysRole")
	@ResponseBody
	public SysRole getSysRole(SysRole sysRole) {
		return sysRoleService.selectByPrimaryKey(sysRole.getRoleUid());
	}
	
	@RequestMapping("/updateSysRole")
	@ResponseBody
	public String updateSysRole(SysRole sysRole) {
		try {	
			sysRoleService.updateByPrimaryKeySelective(sysRole);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@ResponseBody  
	@RequestMapping(value="roleexists")
	public boolean roleexists(SysRole sysRole) {
	 String encode = "iso8859-1";      
	 String roleName = sysRole.getRoleName();
	 String roleNameNew = sysRole.getRoleName();
	 String roleUid = sysRole.getRoleUid();
       try {      
           if (roleName.equals(new String(roleName.getBytes(encode), encode))) {      //判断是不是GB2312
        	   roleNameNew=new String(roleNameNew.getBytes("iso8859-1"),"utf-8");
            }      
           sysRole.setRoleName(roleNameNew);
           SysRole role = sysRoleService.select(sysRole);
	   		if(role!=null){
	   			if(StringUtils.isNotBlank(roleUid)) {
	   				if(roleUid.equals(role.getRoleUid())) {
	   					return true;
	   				}
	   			};
	   			return false;  
	   		}
        } catch (Exception e) {      
        	return false;
        }      
		return true;  
	}
	
	
	
	@RequestMapping("/addSysRole")
	@ResponseBody
	public String addSysRole(SysRole sysRole) {
		try {	
			sysRole.setRoleUid("sysRole:"+UUIDTool.getUUID());
			sysRoleService.insert(sysRole);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysRole")
	@ResponseBody
	public String deleteSysRole(String roleUid) {
		try {	
			sysRoleService.deleteByPrimaryKey(roleUid);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
}
