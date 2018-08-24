package com.desmart.desmartsystem.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.JSTreeNode;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.entity.TreeNode;
import com.desmart.desmartsystem.service.SysDepartmentService;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.desmart.desmartsystem.util.PagedResult;

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
	@Autowired
	private SysUserDepartmentService sysUserDepartmentService;
	
	@RequestMapping(value="/chooseDepartment")
	public ModelAndView chooseDepartment(String elementId) {
		ModelAndView mv = new ModelAndView("desmartsystem/usermanagement/chooseDepartment");
		mv.addObject("elementId",elementId);
		return mv;
	}
	
	@RequestMapping(value = "/queryDepartByNoAndName")
	@ResponseBody
	public ServerResponse queryDepartByNoAndName(SysDepartment sysDepartment) {
		try {
			return sysDepartmentService.queryDepartByNoAndName(sysDepartment);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
	
	@RequestMapping(value = "/queryDepartParentsByDepartId")
	@ResponseBody
	public ServerResponse queryDepartParentsByDepartId(String departUid) {
		try {
			return sysDepartmentService.queryDepartParentsByDepartId(departUid);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
	
	@RequestMapping(value="/allSysDepartment")
	@ResponseBody
	public PagedResult<SysDepartment> allSysDepartment(SysDepartment sysDepartment,Integer pageNo,Integer pageSize){
		PagedResult<SysDepartment> queryByPage=sysDepartmentService.queryByPage(sysDepartment,pageNo,pageSize);
		return queryByPage; 
	}
	
	/**
	 * 移动端带分页查询除某个部门外的部门列表
	 * @param sysDepartment
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/allSysDepartmentMove")
	@ResponseBody
	public ServerResponse allSysDepartmentMove(String departUid,
			Integer pageNo,Integer pageSize,String condition){
		try {
			return sysUserDepartmentService.allSysDepartmentMove(departUid,pageNo,pageSize,condition);
		}catch(Exception e) {
			e.printStackTrace();
			return ServerResponse.createByError();
		}
	}
	
	@RequestMapping(value="/treeDisplay")
	@ResponseBody
	public List<TreeNode> treeDisplay(TreeNode treeNode){
		System.out.println(treeNode);
		return sysDepartmentService.selectTree(treeNode); 
	}
	
	/**
	 * 根据传入的父级组织id获得子级组织集合
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/selectDepartmentTreeNodeByParent")
	@ResponseBody
	public List<JSTreeNode> selectDepartmentTreeNodeByParent(String id){
		return sysDepartmentService.selectDepartmentTreeNodeByParent(id);
	}
	
	@RequestMapping("/getSysDepartment")
	@ResponseBody
	public SysDepartment getSysDepartment(SysDepartment sysDepartment) {
		return sysDepartmentService.findById(sysDepartment);
	}
	
	@ResponseBody  
	@RequestMapping(value="departmentExists")
	public boolean departmentExists(SysDepartment sysDepartment) {
		if(sysDepartmentService.select(sysDepartment)!=null){
			return false;  
		}
		return true;  
	}
	
	@RequestMapping("/updateSysDepartment")
	@ResponseBody
	public String updateSysDepartment(SysDepartment sysDepartment) {
		try {	
			String admins[] = sysDepartment.getDepartAdmins().split(";");
			for (String string : admins) {
				sysDepartment.setDepartAdmins(string);
			}
			sysDepartmentService.update(sysDepartment);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace(); 
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/insertSysDepartment")
	@ResponseBody
	public String insertSysDepartment(SysDepartment sysDepartment) {
		try {	
			String admins[] = sysDepartment.getDepartAdmins().split(";");
			for (String string : admins) {
				sysDepartment.setDepartAdmins(string);
			}
			sysDepartmentService.insert(sysDepartment);
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
