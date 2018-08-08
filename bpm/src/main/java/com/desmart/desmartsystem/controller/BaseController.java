package com.desmart.desmartsystem.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.SysCompany;
import com.desmart.desmartsystem.task.CompanySys;

/**
 * @功能说明：
 * @作者： herun
 * @创建日期：2015-09-25
 * @版本号：V1.0
 */
@Controller
@RequestMapping("/baseController")
public class BaseController {
	
	private Logger logger = Logger.getLogger(BaseController.class);

	//接口日志
	public void interfaceLog() {
		
	}
	private CompanySys companySys;
	
	// sap查询公司 数据集 返回给前台
	@RequestMapping("searchCompany")
	@ResponseBody
	public List<SysCompany> searchCompanyList() {
		return companySys.executeSysCompany();
	}
}


