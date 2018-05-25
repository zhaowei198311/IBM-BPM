package com.desmart.desmartsystem.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.SysDictCd;
import com.desmart.desmartsystem.service.SysDictCdService;
import com.desmart.desmartsystem.util.PagedResult;
import com.desmart.desmartsystem.util.UUIDTool;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-05-24
 */
@Controller
@RequestMapping("/sysDictCd")
public class SysDictCdController {
	@Autowired
	private SysDictCdService sysDictCdService;
	
	
	
	@RequestMapping(value="/dictCdList")
	public String dictCdList(){
		return "desmartsystem/usermanagement/dictCdList";
	}
	
	
	@RequestMapping(value="/allSysDictCd")
	@ResponseBody
	public PagedResult<SysDictCd> allSysDictCd(SysDictCd sysDictCd,Integer pageNo,Integer pageSize){
		PagedResult<SysDictCd> queryByPage=sysDictCdService.queryByPage(sysDictCd,pageNo,pageSize);
		return queryByPage; 
	}
	
	
	
	@RequestMapping("/getSysDictCd")
	@ResponseBody
	public SysDictCd getSysDictCd(SysDictCd sysDictCd) {
		return sysDictCdService.findById(sysDictCd);
	}
	
	@RequestMapping("/updateSysDictCd")
	@ResponseBody
	public String updateSysDictCd(SysDictCd sysDictCd) {
		try {	
			sysDictCdService.update(sysDictCd);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/addSysDictCd")
	@ResponseBody
	public String addSysDictCd(SysDictCd sysDictCd) {
		try {	
			sysDictCd.setDcUid("sysDcUid"+UUIDTool.getUUID());
			sysDictCdService.insert(sysDictCd);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	@RequestMapping("/deleteSysDictCd")
	@ResponseBody
	public String deleteSysDictCd(SysDictCd sysDictCd) {
		try {	
			sysDictCdService.delete(sysDictCd);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
