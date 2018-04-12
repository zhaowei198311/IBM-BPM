package com.desmart.desmartbpm.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.entity.SysDepartment;
import com.desmart.desmartbpm.entity.TreeNode;
import com.desmart.desmartbpm.service.SysDepartmentService;
import com.desmart.desmartbpm.util.PagedResult;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
@Controller
@RequestMapping("/sysDepartment")
public class SysDepartmentController {
	@Autowired
	private SysDepartmentService sysDepartmentService;
	
	@RequestMapping(value="/allSysDepartment")
	@ResponseBody
	public PagedResult<SysDepartment> allSysDepartment(SysDepartment sysDepartment,Integer pageNo,Integer pageSize){
		PagedResult<SysDepartment> queryByPage=sysDepartmentService.queryByPage(sysDepartment,pageNo,pageSize);
		return queryByPage; 
	}
	
	
	@RequestMapping(value="/treeDisplay")
	@ResponseBody
	public List<TreeNode> treeDisplay(TreeNode treeNode){
		return sysDepartmentService.selectTree(treeNode); 
	}
	
	
	@RequestMapping("/getSysDepartment")
	@ResponseBody
	public SysDepartment getSysDepartment(SysDepartment sysDepartment) {
		return sysDepartmentService.findById(sysDepartment);
	}
	
	@RequestMapping("/updateSysDepartment")
	@ResponseBody
	public String updateSysDepartment(SysDepartment sysDepartment) {
		try {	
			sysDepartmentService.update(sysDepartment);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysDepartment")
	@ResponseBody
	public String deleteSysDepartment(SysDepartment sysDepartment) {
		try {	
			sysDepartmentService.delete(sysDepartment);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
