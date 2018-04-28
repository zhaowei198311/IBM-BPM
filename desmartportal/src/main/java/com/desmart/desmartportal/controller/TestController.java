/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**  
* <p>Title: TestController</p>  
* <p>Description: </p>  
* @author zhaowei 
* @date 2018年4月27日  
*/
@Controller
@RequestMapping("/test")
public class TestController {
	
	@RequestMapping("/index")
	public String  index() {
		return "index";
	}
	
	@RequestMapping("/backlog")
	public String  backlog() {
		return "backlog";
	}
}
