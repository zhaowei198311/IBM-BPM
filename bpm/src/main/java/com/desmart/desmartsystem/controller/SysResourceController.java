package com.desmart.desmartsystem.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartsystem.entity.SysResource;
import com.desmart.desmartsystem.entity.TreeNode;
import com.desmart.desmartsystem.service.SysResourceService;
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
@RequestMapping("/sysResource")
public class SysResourceController {
	
	@Autowired
	private SysResourceService sysResourceService;
	
	@RequestMapping(value="/resource_list") 
	public String index(){
		return "desmartsystem/usermanagement/resource_list";
	}
	
	@RequestMapping("/resource")
	public ModelAndView resource(String resouceUid){
	    ModelAndView mav=new ModelAndView("desmartsystem/usermanagement/resource");
	    mav.addObject("resouceUid", resouceUid);
	    return mav;
	}
	
	@RequestMapping(value="/allSysResource")
	@ResponseBody
	public PagedResult<SysResource> allSysResource(SysResource sysResource,Integer pageNo,Integer pageSize){
		PagedResult<SysResource> queryByPage=sysResourceService.queryByPage(sysResource,pageNo,pageSize);
		return queryByPage; 
	}
	
	
	@RequestMapping(value="/resourceTree")
	@ResponseBody
	public List<TreeNode> resourceTree(){
		return sysResourceService.resourceTree(); 
	}
	
	
	
	@RequestMapping("/getSysResource")
	@ResponseBody
	public SysResource getSysResource(SysResource sysResource) {
		return sysResourceService.findById(sysResource);
	}
	
	@RequestMapping("/updateSysResource")
	@ResponseBody
	public String updateSysResource(SysResource sysResource) {
		try {	
			sysResourceService.update(sysResource);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping(value="/addSysResource")
	@ResponseBody
	public String addSysResource(SysResource sysResource){
		try {
			sysResource.setResouceUid("sysResource:"+UUIDTool.getUUID());
			sysResource.setOrderIndex(1);
			
			if (StringUtils.isEmpty(sysResource.getParentId())) {
				sysResource.setParentId("0");
				sysResource.setResouceType(0);
			}else{
				sysResource.setResouceType(1);
			}
			
			sysResourceService.insert(sysResource);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysResource")
	@ResponseBody
	public String deleteSysResource(SysResource sysResource) {
		try {	
			sysResourceService.delete(sysResource);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	
}
