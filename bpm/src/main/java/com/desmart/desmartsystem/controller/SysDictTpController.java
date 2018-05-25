package com.desmart.desmartsystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.SysDictTp;
import com.desmart.desmartsystem.service.SysDictTpService;
import com.desmart.desmartsystem.util.PagedResult;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-05-24
 */
@Controller
@RequestMapping("/sysDictTp")
public class SysDictTpController {
	@Autowired
	private SysDictTpService sysDictTpService;
	
	@RequestMapping(value="/allSysDictTp")
	@ResponseBody
	public PagedResult<SysDictTp> allSysDictTp(SysDictTp sysDictTp,Integer pageNo,Integer pageSize){
		PagedResult<SysDictTp> queryByPage=sysDictTpService.queryByPage(sysDictTp,pageNo,pageSize);
		return queryByPage; 
	}
	
	
	@RequestMapping("/getSysDictTp")
	@ResponseBody
	public SysDictTp getSysDictTp(SysDictTp sysDictTp) {
		return sysDictTpService.findById(sysDictTp);
	}
	
	@RequestMapping("/updateSysDictTp")
	@ResponseBody
	public String updateSysDictTp(SysDictTp sysDictTp) {
		try {	
			sysDictTpService.update(sysDictTp);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/addSysDictTp")
	@ResponseBody
	public String addSysDictTp(SysDictTp sysDictTp) {
		try {	
			sysDictTpService.insert(sysDictTp);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysDictTp")
	@ResponseBody
	public String deleteSysDictTp(SysDictTp sysDictTp) {
		try {	
			sysDictTpService.delete(sysDictTp);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
