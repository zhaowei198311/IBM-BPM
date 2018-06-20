package com.desmart.desmartsystem.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}


