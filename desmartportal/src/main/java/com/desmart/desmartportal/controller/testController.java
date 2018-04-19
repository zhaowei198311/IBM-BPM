/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**  
* <p>Title: testController</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月19日  
*/
@Controller
@RequestMapping(value = "/test")
public class testController {
	
	private Logger LOG = Logger.getLogger(testController.class);
	
	@RequestMapping(value = "/index")
	public String index() {
		LOG.info("跳转首页");
		return "index";
	}
}
