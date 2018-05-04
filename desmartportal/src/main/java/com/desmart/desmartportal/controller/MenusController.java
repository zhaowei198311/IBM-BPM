/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**  
* <p>Title: 跳转WEB-INFO下jsp页面的 控制层</p>  
* <p>Description: </p>  
* @author zhaowei 
* @date 2018年4月27日  
*/
@Controller
@RequestMapping("/test")
public class MenusController {
	
	@RequestMapping("/index")
	public String  index() {
		return "index";
	}
	
	@RequestMapping("/backlog")
	public String  backlog() {
		return "backlog";
	}
	
	@RequestMapping("/backlogDetail")
	public String backlogDetail() {
		return "backlog_detail";
	}
}
