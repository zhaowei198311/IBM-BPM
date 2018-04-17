package com.desmart.desmart_systemmanagement.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmart_systemmanagement.entity.SysTeamMember;
import com.desmart.desmart_systemmanagement.service.SysTeamMemberService;

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
			sysTeamMemberService.deleteByPrimaryKey(sysTeamMember.getTeamUid());
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
	
}
