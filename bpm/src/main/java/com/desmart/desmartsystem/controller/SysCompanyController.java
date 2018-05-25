package com.desmart.desmartsystem.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.SysCompany;
import com.desmart.desmartsystem.service.SysCompanyService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsf
 * @since 2018-05-22
 */
@Controller
@RequestMapping("/sysCompany")
public class SysCompanyController {
	
	//allCompany
	
	@Autowired
	private	SysCompanyService sysCompanyService;
	
	@RequestMapping(value="/allCompany")
	@ResponseBody
	public List<SysCompany> allCompany(SysCompany sysCompany) {
		return sysCompanyService.selectAll(sysCompany);
	}
}
