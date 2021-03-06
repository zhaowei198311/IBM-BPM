package com.desmart.desmartsystem.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartsystem.entity.SysRole;
import com.desmart.desmartsystem.entity.SysTeam;
import com.desmart.desmartsystem.service.SysTeamService;
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
@RequestMapping("/sysTeam")
public class SysTeamController {
	@Autowired
	private SysTeamService sysTeamService;
	
	@RequestMapping(value="/group")
	public String role(){
		return "desmartsystem/usermanagement/group";
	}
	
	
	@RequestMapping(value="/teamBindingRole")
	public ModelAndView teamBindingRole(String teamUid){
		ModelAndView ml =new ModelAndView("desmartsystem/usermanagement/teamBindingRole");
		ml.addObject("teamUid", teamUid);
		return ml;
	}
	
	@RequestMapping(value="/allSysTeam")
	@ResponseBody
	public PagedResult<SysTeam> allSysTeam(SysTeam sysTeam,Integer pageNo,Integer pageSize){
		PagedResult<SysTeam> queryByPage=sysTeamService.queryByPage(sysTeam,pageNo,pageSize);
		return queryByPage; 
	}
	
	
	@RequestMapping(value="/sysTeamList")
	@ResponseBody
	public List<SysTeam> sysTeamList(SysTeam sysTeam){
		return sysTeamService.selectAll(sysTeam);
	}
	
	@RequestMapping(value="/selectFilterNode")
	@ResponseBody
	public List<SysTeam> selectFilterNode(SysTeam sysTeam){
		return sysTeamService.selectFilterNode(sysTeam);
	}
	
	
	
	@RequestMapping("/getSysTeam")
	@ResponseBody
	public SysTeam getSysTeam(SysTeam sysTeam) {
		return sysTeamService.selectByPrimaryKey(sysTeam.getTeamUid());
	}
	
	
	@RequestMapping("/getSysTeamRole")
	@ResponseBody
	public SysTeam getSysTeamRole(SysTeam sysTeam) {
		SysTeam  sysTeam1=sysTeamService.selectByPrimary(sysTeam);
		if(sysTeam1==null) {
			return sysTeam;
		}
		return sysTeamService.selectByPrimary(sysTeam);
	}
	
	@RequestMapping("/updateSysTeam") 
	@ResponseBody
	public String updateSysTeam(SysTeam sysTeam) {
		try {	
			sysTeamService.updateByPrimaryKeySelective(sysTeam);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@ResponseBody  
	@RequestMapping(value="teamexists")
	public boolean teamexists(SysTeam sysTeam) {
	 String encode = "iso8859-1";      
	 String teamName = sysTeam.getTeamName();
	 String teamNameNew = sysTeam.getTeamName();
	 String teamUid = sysTeam.getTeamUid();
       try {      
           if (teamName.equals(new String(teamName.getBytes(encode), encode))) {      //判断是不是GB2312
        	   teamNameNew=new String(teamNameNew.getBytes("iso8859-1"),"utf-8");
            }      
           sysTeam.setTeamName(teamNameNew);
           
           SysTeam team = sysTeamService.select(sysTeam);
           
	   		if(team!=null){
	   			if(StringUtils.isNotBlank(teamUid)) {
	   				if(teamUid.equals(team.getTeamUid())) {
	   					return true;
	   				};
	   			}
	   			return false;  
	   		}
        } catch (Exception e) {      
        	return false;
        }      
		return true;  
	}
	
	@RequestMapping("/addSysTeam")
	@ResponseBody
	public String addSysTeam(SysTeam sysTeam) {
		try {	
			sysTeam.setTeamUid("sysTeam:"+UUIDTool.getUUID());
			sysTeamService.insert(sysTeam);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysTeam")
	@ResponseBody
	public String deleteSysTeam(String teamUid) {
		try {	
			sysTeamService.deleteByPrimaryKey(teamUid);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
