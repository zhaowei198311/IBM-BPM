package com.desmart.desmartsystem.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.service.SysTeamMemberService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
@Controller
@RequestMapping("/sysTeamMember")
public class SysTeamMemberController {
	@Autowired
	private SysTeamMemberService sysTeamMemberService;
	
	@RequestMapping(value="/allSysTeamMember")
	@ResponseBody
	public List<SysTeamMember> allSysRoleResource(SysTeamMember sysTeamMember){
		return sysTeamMemberService.selectAll(sysTeamMember);
	}
	
	@RequestMapping("/addSysTeamMember")
	@ResponseBody
	public String adSysTeamMember(SysTeamMember sysTeamMember) {
		try {	
			//sysTeamMemberService.delete(sysTeamMember);
			String userUid=sysTeamMember.getUserUid();
			if(StringUtils.isNotBlank(userUid)) {
				String[]  roleUser=userUid.split(",");
				for (String string : roleUser) {
					sysTeamMember.setUserUid(string);
					sysTeamMemberService.insert(sysTeamMember);
				}
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	
	
	//批量删除
	@RequestMapping("/deleteSysTeamMembers")
	@ResponseBody
	public String deleteSysTeamMembers(@RequestParam(value="memberUids[]") String[] memberUids) {
		try {
			for (String string : memberUids) {
				sysTeamMemberService.deleteByPrimaryKey(string);
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
}
