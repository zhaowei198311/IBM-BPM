package com.desmart.desmartsystem.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.SysUserService;
import com.desmart.desmartsystem.util.PagedResult;
import com.desmart.desmartsystem.util.UUIDTool;

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
	
	@RequestMapping(value="/select_personnel")
	public ModelAndView select_personnel(String id, String isSingle){
		ModelAndView model = new ModelAndView("usermanagement/select_personnel");
		model.addObject("id","a");
		model.addObject("isSingle","false");
		return model;
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
	
	@RequestMapping(value="/userList")
	@ResponseBody
	public List<SysUser> userList(SysUser sysUser){
		return sysUserService.selectAll(sysUser);
	}
	
	
	@RequestMapping(value = "/userDetail")
    public ModelAndView userDetail(SysUser sysUser) {
		ModelAndView moAndView=new ModelAndView("usermanagement/edit_user");
		moAndView.addObject("sysUser",sysUserService.findById(sysUser));
        return moAndView;
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
	
	@RequestMapping("/addSysUser")
	@ResponseBody
	public String addSysUser(SysUser sysUser) {
		try {	
			sysUser.setUserUid("sysUser"+UUIDTool.getUUID());
			sysUserService.insert(sysUser);
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
